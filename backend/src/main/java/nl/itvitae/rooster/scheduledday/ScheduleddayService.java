package nl.itvitae.rooster.scheduledday;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import nl.itvitae.rooster.MyDay;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomRepository;
import nl.itvitae.rooster.lesson.Lesson;
import nl.itvitae.rooster.lesson.LessonRepository;
import nl.itvitae.rooster.teacher.Teacher;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleddayService {

  private final ScheduleddayRepository scheduleddayRepository;
  private final ClassroomRepository classroomRepository;
  private final LessonRepository lessonRepository;

  public List<Scheduledday> findAll() {
    return scheduleddayRepository.findAll();
  }

  public List<Scheduledday> findAllByMonth(int month, int year) {
    LocalDate startDate = LocalDate.of(year, month, 1);
    LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

    return scheduleddayRepository.findByDateBetween(startDate, endDate);
  }

  public Scheduledday addScheduledday(LocalDate date, Classroom classroom, Lesson lesson) {
    Scheduledday scheduledday = new Scheduledday(date, classroom, lesson);
    preventConflicts(scheduledday, false);
    return scheduleddayRepository.save(scheduledday);
  }

  private void preventConflicts(Scheduledday scheduledday, boolean looped) {
    LocalDate date = scheduledday.getDate();
    Lesson lesson = scheduledday.getLesson();
    Classroom classroom = scheduledday.getClassroom();
    boolean isClassroomFull =
        classroom.getCapacity() < lesson.getGroup().getNumberOfStudents();
    if (scheduleddayRepository.existsByDateAndLessonGroup(date, lesson
        .getGroup())) {
      if (date.getDayOfWeek() != DayOfWeek.FRIDAY) {
        scheduledday.setDate(date.plusDays(1));
        preventConflicts(scheduledday, looped);
      } else {
        scheduledday.setDate(date.minusDays(4));
        preventConflicts(scheduledday, true);
      }
    }
    if (isClassroomFull || scheduleddayRepository.existsByDateAndClassroom(scheduledday.getDate(),
        classroom)) {
      int nextClassroomId = (classroom.getId() == 3 || classroom.getId() == 4) ? 2 : 1;
      Optional<Classroom> newClassroom = classroomRepository.findById(
          classroom.getId() + nextClassroomId);
      if (newClassroom.isPresent()) {
        scheduledday.setClassroom(newClassroom.get());
      } else {
        lesson.setPracticum(false);
        scheduledday.setClassroom(classroomRepository.findById(1L).get());
      }
      preventConflicts(scheduledday, looped);
    }

    //if a group has teachers available
    if (!looped && lesson.getGroup().getTeachers().size() != 0) {
      for (Teacher teacher : lesson.getGroup().getTeachers()) {

        //if teacher can be assigned, find an available date for the lesson and assign the teacher
        if (teacher.isTeachesPracticum() == lesson.isPracticum()) {
          boolean teacherAvailable = false;

          //check whether teacher is already working maximum amount of days this week
          int lessons = 0;
          int dayOfWeek = date.getDayOfWeek().getValue();
          for (Scheduledday otherScheduledDay : scheduleddayRepository.findByDateBetween(
              date.minusDays(dayOfWeek - 1), date.plusDays(5 - dayOfWeek))) {
            Teacher otherTeacher = otherScheduledDay.getLesson().getTeacher();
            if (otherTeacher != null && teacher.getId().equals(otherTeacher.getId())) {
              lessons++;
            }
          }
          if (lessons >= teacher.getMaxDaysPerWeek()) {
            break;
          }

          //check whether teacher is available to work this day
          days:
          for (MyDay day : teacher.getAvailability()) {
            if (day.getDay().equals(date.getDayOfWeek())) {

              //check if teacher is already teaching another lesson this day
              for (Scheduledday otherScheduledDay : scheduleddayRepository.findByDate(
                  scheduledday.getDate())) {
                Teacher otherTeacher = otherScheduledDay.getLesson().getTeacher();
                if (otherTeacher != null && teacher.getId().equals(otherTeacher.getId())) {
                  break days;
                }
              }

              //assign teacher to this lesson
              lesson.setTeacher(teacher);
              lessonRepository.save(lesson);
              teacherAvailable = true;
              break;
            }
          }

          //if teacher isn't available and it's not the end of the week, check the next day
          if (!teacherAvailable && date.getDayOfWeek() != DayOfWeek.FRIDAY) {
            scheduledday.setDate(date.plusDays(1));
            preventConflicts(scheduledday, looped);
          }
        }
      }
    }
  }

  public ByteArrayInputStream createExcel(int year) throws IOException {
    List<Scheduledday> scheduledDays = scheduleddayRepository.findByDateBetween(LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31));
    Workbook workbook = new XSSFWorkbook();

    for (int i = 1; i <= 12; i++) {
      LocalDate currentDate = LocalDate.of(year, i, 1);
      Sheet sheet = workbook.createSheet(currentDate.getMonth().toString() + year);

      Row header = sheet.createRow(0);
      for (int j = 1; j <= currentDate.lengthOfMonth(); j++) {
        for (int k = 1; k <= 6; k++) {
          Cell cell = header.createCell(k);
          if (j == 1) {
            cell.setCellValue("Lokaal " + k);
          }
        }
        header = sheet.createRow(j);
      }
    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    workbook.write(outputStream);
    workbook.close();
    return new ByteArrayInputStream(outputStream.toByteArray());
  }

}