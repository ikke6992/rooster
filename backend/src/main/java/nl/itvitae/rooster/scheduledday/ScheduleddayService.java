package nl.itvitae.rooster.scheduledday;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


import lombok.AllArgsConstructor;
import nl.itvitae.rooster.MyDay;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomRepository;
import nl.itvitae.rooster.freeday.FreeDay;
import nl.itvitae.rooster.freeday.FreeDayRepository;
import nl.itvitae.rooster.group.Group;
import nl.itvitae.rooster.lesson.Lesson;
import nl.itvitae.rooster.lesson.LessonRepository;
import nl.itvitae.rooster.teacher.Teacher;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleddayService {

  private final ScheduleddayRepository scheduleddayRepository;
  private final ClassroomRepository classroomRepository;
  private final LessonRepository lessonRepository;
  private final FreeDayRepository freeDayRepository;

  public List<Scheduledday> findAll() {
    return scheduleddayRepository.findAll();
  }

  public Scheduledday findById(long id) {
    return scheduleddayRepository.findById(id).get();
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

  public OverrideDTO overrideScheduling(
      Scheduledday scheduledday, LocalDate date, Classroom classroom, boolean adaptWeekly) {

    //create successes list and failures map for the OverrideDTO
    List<ScheduleddayDTO> successes = new ArrayList<>();
    Map<ScheduleddayDTO, String> failures = new HashMap<>();

    //save original date and classroom for later use
    LocalDate oldDate = scheduledday.getDate();
    Classroom oldClassroom = scheduledday.getClassroom();

    //override scheduledday, validity of override already checked in controller
    scheduledday.setDate(date);
    scheduledday.setClassroom(classroom);
    scheduleddayRepository.save(scheduledday);
    successes.add(new ScheduleddayDTO(scheduledday));

    //if requested, keep overriding the scheduledday weekly
    if (adaptWeekly) {
      oldDate = oldDate.plusWeeks(1);
      LocalDate newDate = date.plusWeeks(1);
      Group group = scheduledday.getLesson().getGroup();

      //continue checking as long as scheduling exists on this date and classroom or the date is a free day
      while (scheduleddayRepository.existsByDateAndClassroom(oldDate, oldClassroom)
          || freeDayRepository.existsByDate(oldDate)) {
        if (scheduleddayRepository.existsByDateAndClassroom(oldDate, oldClassroom)) {
          Scheduledday nextScheduledday = scheduleddayRepository.findByDateAndClassroom(oldDate, oldClassroom).get();

          //check if the nextScheduledday found is a scheduledday for the correct group
          if (nextScheduledday.getLesson().getGroup().getGroupNumber() == group.getGroupNumber()) {

            //if newDate is a free day, delete scheduling and add to successes list
            if (freeDayRepository.existsByDate(newDate)) {
              scheduleddayRepository.delete(nextScheduledday);
              successes.add(new ScheduleddayDTO(nextScheduledday));
            } else {

              //try to override nextScheduledday, add to successes if it can be done or to failures if it can't be done
              try {
                if (scheduleddayRepository.existsByDateAndClassroom(newDate, classroom)) {
                  throw new OverrideException(
                      String.format("Classroom %d already in use on %s", classroom.getId(), newDate));
                } else if (!newDate.equals(oldDate)
                    && scheduleddayRepository.existsByDateAndLessonGroup(newDate, group)) {
                  throw new OverrideException(
                      String.format("Group %d already has a lesson on %s", group.getGroupNumber(), newDate));
                }
                nextScheduledday.setDate(newDate);
                nextScheduledday.setClassroom(classroom);
                scheduleddayRepository.save(nextScheduledday);
                successes.add(new ScheduleddayDTO(nextScheduledday));
              } catch (OverrideException e) {
                failures.put(new ScheduleddayDTO(nextScheduledday), e.getMessage());
              }
            }
          }
        }
        oldDate = oldDate.plusWeeks(1);
        newDate = newDate.plusWeeks(1);
      }
    }

    return new OverrideDTO(successes, failures);
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
    LocalDate startDate = LocalDate.of(year, 1, 1);
    LocalDate endDate = LocalDate.of(year, 12, 31);
    List<Scheduledday> scheduledDays = scheduleddayRepository.findByDateBetween(startDate, endDate);
    if (scheduledDays.isEmpty()) return null;
    List<FreeDay> freedays = freeDayRepository.findByDateBetween(startDate, endDate);
    Workbook workbook = new XSSFWorkbook();
    for (int i = 1; i <= 12; i++) {
      LocalDate currentDate = LocalDate.of(year, i, 1);
      Sheet sheet = workbook.createSheet(currentDate.getMonth().toString() + year);

      Row header = sheet.createRow(0);
      for (int j = 1; j <= 6; j++) {
        Cell cell = header.createCell(j);
        cell.setCellValue("Lokaal " + j);
      }
      int monthLength = currentDate.lengthOfMonth();
      for (int j = 1; j <= monthLength; j++) {
        Row row = sheet.createRow(j);
        sheet.setColumnWidth(0, 15 * 256);

        Cell cell = row.createCell(0);
        cell.setCellValue(currentDate.toString());
        LocalDate finalCurrentDate = currentDate;
        if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
          CellStyle weekendGreen = workbook.createCellStyle();
          weekendGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
          org.apache.poi.ss.usermodel.Color color = new XSSFColor(
              new java.awt.Color(181, 230, 162), new DefaultIndexedColorMap());
          weekendGreen.setFillForegroundColor(color);
          cell.setCellStyle(weekendGreen);
          for (int k = 1; k <= 6; k++) {
            Cell cell1 = row.createCell(k);
            cell1.setCellStyle(weekendGreen);
          }
          currentDate = currentDate.plusDays(1);
          continue;
        }
        if (freedays.stream().anyMatch((freeDay -> freeDay.getDate().equals(finalCurrentDate)))) {
          CellStyle freeDayYellow = workbook.createCellStyle();
          freeDayYellow.setFillPattern(FillPatternType.SOLID_FOREGROUND);
          org.apache.poi.ss.usermodel.Color color = new XSSFColor(
              new java.awt.Color(255, 255, 0), new DefaultIndexedColorMap());
          freeDayYellow.setFillForegroundColor(color);
          cell.setCellStyle(freeDayYellow);
          for (int k = 1; k <= 6; k++) {
            Cell cell1 = row.createCell(k);
            cell1.setCellStyle(freeDayYellow);
          }
          currentDate = currentDate.plusDays(1);
          continue;
        }

        for (int k = 1; k <= 6; k++) {
          sheet.setColumnWidth(k, 25 * 256);
          Cell cell1 = row.createCell(k);

          int finalK = k;
          List<Scheduledday> scheduleddaysFiltered = scheduledDays.stream().filter(
                  day -> day.getDate().equals(finalCurrentDate) && day.getClassroom().getId() == finalK)
              .toList();
          if (!scheduleddaysFiltered.isEmpty()) {
            Scheduledday scheduledday = scheduleddaysFiltered.getFirst();
            cell1.setCellValue("Group " + scheduledday.getLesson().getGroup().getGroupNumber() + " "
                + scheduledday.getLesson().getGroup().getField());
            String hexColour = scheduledday.getLesson().getGroup().getColor();
            int hexR = Integer.valueOf(hexColour.substring(1, 3), 16);
            int hexG = Integer.valueOf(hexColour.substring(3, 5), 16);
            int hexB = Integer.valueOf(hexColour.substring(5, 7), 16);
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            org.apache.poi.ss.usermodel.Color color = new XSSFColor(
                new java.awt.Color(hexR, hexG, hexB), new DefaultIndexedColorMap());
            cellStyle.setFillForegroundColor(color);
            cell1.setCellStyle(cellStyle);
          }
        }
        currentDate = currentDate.plusDays(1);
      }
    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    workbook.write(outputStream);
    workbook.close();
    return new ByteArrayInputStream(outputStream.toByteArray());
  }

}

class OverrideException extends Exception {
  public OverrideException(String msg) {
    super(msg);
  }
}