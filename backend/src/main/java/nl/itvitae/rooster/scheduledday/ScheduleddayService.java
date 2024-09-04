package nl.itvitae.rooster.scheduledday;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import nl.itvitae.rooster.MyDay;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomRepository;
import nl.itvitae.rooster.lesson.Lesson;
import nl.itvitae.rooster.lesson.LessonRepository;
import nl.itvitae.rooster.teacher.Teacher;
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
    preventConflicts(scheduledday);
    return scheduleddayRepository.save(scheduledday);
  }

  private void preventConflicts(Scheduledday scheduledday) {
    LocalDate date = scheduledday.getDate();
    Lesson lesson = scheduledday.getLesson();
    Classroom classroom = scheduledday.getClassroom();
    boolean isClassroomFull =
        classroom.getCapacity() < lesson.getGroup().getNumberOfStudents();
    if (scheduleddayRepository.existsByDateAndLessonGroup(date, lesson
        .getGroup())) {
      if (date.getDayOfWeek() != DayOfWeek.FRIDAY) {
        scheduledday.setDate(date.plusDays(1));
        preventConflicts(scheduledday);
      } else {
        scheduledday.setDate(date.minusDays(4));
        preventConflicts(scheduledday);
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
      preventConflicts(scheduledday);
    }

    //if a group has teachers available
    if (lesson.getGroup().getTeachers().size() != 0) {
      teacher: for (Teacher teacher : lesson.getGroup().getTeachers()) {

        //if teacher can be assigned, find an available date for the lesson and assign the teacher
        if (teacher.isTeachesPracticum() == lesson.isPracticum()) {
          boolean teacherAvailable = false;
          int lessons = 0;
          for (Scheduledday otherScheduledDay : scheduleddayRepository.findByDateBetween(
              date.minusDays(date.getDayOfWeek().getValue()-1), date.plusDays(5-date.getDayOfWeek().getValue()))
          ) {
            Teacher otherTeacher = otherScheduledDay.getLesson().getTeacher();
            if (otherTeacher != null && teacher.getId().equals(otherTeacher.getId())) {
              lessons++;
            }
          }
          if (lessons >= teacher.getMaxDaysPerWeek()) {
            break teacher;
          }
          day: for (MyDay day : teacher.getAvailability()) {
            if (day.getDay().equals(date.getDayOfWeek())) {
              for (Scheduledday otherScheduledDay : scheduleddayRepository.findByDate(scheduledday.getDate())) {
                Teacher otherTeacher = otherScheduledDay.getLesson().getTeacher();
                if (otherTeacher != null && teacher.getId().equals(otherTeacher.getId())) {
                  break day;
                }
              }
              lesson.setTeacher(teacher);
              lessonRepository.save(lesson);
              teacherAvailable = true;
              break;
            }
          }

          if (!teacherAvailable && date.getDayOfWeek() != DayOfWeek.FRIDAY) {
            scheduledday.setDate(date.plusDays(1));
            preventConflicts(scheduledday);
          }
        }
      }
    }
  }

}