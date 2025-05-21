package nl.itvitae.rooster.scheduledday;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


import lombok.AllArgsConstructor;
import nl.itvitae.rooster.MyDay;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomRepository;
import nl.itvitae.rooster.freeday.FreeDay;
import nl.itvitae.rooster.freeday.FreeDayRepository;
import nl.itvitae.rooster.group.Group;
import nl.itvitae.rooster.lesson.Lesson;
import nl.itvitae.rooster.lesson.LessonRepository;
import nl.itvitae.rooster.teacher.GroupTeacher;
import nl.itvitae.rooster.teacher.Teacher;
import nl.itvitae.rooster.utils.ColorUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
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

  public Scheduledday addScheduledday(int phase, LocalDate date, Classroom classroom, Lesson lesson) {
    Scheduledday scheduledday = new Scheduledday(date, classroom, lesson);
    preventConflicts(phase, scheduledday, false);
    scheduleddayRepository.save(scheduledday);
    lesson.setScheduledday(scheduledday);
    lessonRepository.save(lesson);
    return scheduledday;
  }

  public OverrideDTO overrideScheduling(
      Scheduledday scheduledday, LocalDate date, Classroom classroom, boolean adaptWeekly) {

    //create successes list and failures map for the OverrideDTO
    List<String> successes = new ArrayList<>();
    List<String> failures = new ArrayList<>();

    //save original date and classroom for later use
    LocalDate oldDate = scheduledday.getDate();
    Classroom oldClassroom = scheduledday.getClassroom();

    //override scheduledday, validity of override already checked in controller
    scheduledday.setDate(date);
    scheduledday.setClassroom(classroom);
    scheduleddayRepository.save(scheduledday);
    successes.add(String.format("Lesson moved from classroom %d on %s to classroom %d on %s",
        oldClassroom.getId(), oldDate, classroom.getId(), date));

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
              successes.add(String.format(
                  "Lesson removed from classroom %d on %s, no new lesson because %s is a free day",
                  oldClassroom.getId(), oldDate, newDate));
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
                successes.add(String.format("Lesson moved from classroom %d on %s to classroom %d on %s",
                    oldClassroom.getId(), oldDate, classroom.getId(), newDate));
              } catch (OverrideException e) {
                failures.add(e.getMessage());
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

  private void preventConflicts(int phase, Scheduledday scheduledday, boolean looped) {
    LocalDate date = scheduledday.getDate();
    Lesson lesson = scheduledday.getLesson();
    Classroom classroom = scheduledday.getClassroom();
    boolean isClassroomFull =
        classroom.getCapacity() < lesson.getGroup().getNumberOfStudents();
    if (isClassroomFull || scheduleddayRepository.existsByDateAndClassroom(scheduledday.getDate(),
        classroom)) {
      int nextClassroomId = (classroom.getId() == 3 || classroom.getId() == 4) ? 2 : 1;
      Optional<Classroom> newClassroom = classroomRepository.findById(
          classroom.getId() + nextClassroomId);
      if (newClassroom.isPresent()) {
        scheduledday.setClassroom(newClassroom.get());
      } else {
        scheduledday.setClassroom(classroomRepository.findById(1L).get());
      }
      preventConflicts(phase, scheduledday, looped);
    }
    if (scheduleddayRepository.existsByDateAndLessonGroup(date, lesson
        .getGroup())) {
      if (date.getDayOfWeek() != DayOfWeek.FRIDAY) {
        scheduledday.setDate(date.plusDays(1));
        preventConflicts(phase, scheduledday, looped);
      } else {
        scheduledday.setDate(date.minusDays(4));
        preventConflicts(phase, scheduledday, true);
      }
    }

    //if a group has teachers available
    if (!looped && lesson.getGroup().getGroupTeachers().size() != 0) {
      for (GroupTeacher groupTeacher : lesson.getGroup().getGroupTeachers()) {
        Teacher teacher = groupTeacher.getTeacher();

        //if teacher can be assigned, find an available date for the lesson and assign the teacher
        boolean teacherAvailable = false;

        //check whether teacher is already working maximum amount of days this week
        int lessons = 0;
        int lessonsForThisGroup = 0;
        int dayOfWeek = date.getDayOfWeek().getValue();
        for (Scheduledday otherScheduledDay : scheduleddayRepository.findByDateBetween(
            date.minusDays(dayOfWeek - 1), date.plusDays(5 - dayOfWeek))) {
          Teacher otherTeacher = otherScheduledDay.getLesson().getTeacher();
          if (otherTeacher != null && teacher.getId().equals(otherTeacher.getId())) {
            lessons++;
            if (otherScheduledDay.getLesson().getGroup().getGroupNumber() == lesson.getGroup().getGroupNumber()) {
              lessonsForThisGroup++;
            }
          }
        }
        if (lessons >= teacher.getMaxDaysPerWeek() ||
            (phase == 1 && lessonsForThisGroup >= groupTeacher.getDaysPhase1()) ||
            (phase == 2 && lessonsForThisGroup >= groupTeacher.getDaysPhase2()) ||
            (phase == 3 && lessonsForThisGroup >= groupTeacher.getDaysPhase3())) {
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
          preventConflicts(phase, scheduledday, looped);
        }
      }
    }
  }

  public ByteArrayInputStream createExcel() throws IOException {
    List<Scheduledday> scheduledDays = scheduleddayRepository.findAll();
    scheduledDays.sort(Comparator.comparing(Scheduledday::getDate));
    if (scheduledDays.isEmpty()) return null;
    List<FreeDay> freedays = freeDayRepository.findAll();
    int year = scheduledDays.getFirst().getDate().getYear();
    int totalYears = scheduledDays.getLast().getDate().getYear() - year + 1;
    Workbook workbook = new XSSFWorkbook();
    for (int i = 1; i <= totalYears; i++, year++) {
      for (int monthNumber = scheduledDays.getFirst().getDate().getMonthValue(); monthNumber <= 12; monthNumber++) {
        createSheet(workbook, year, monthNumber, scheduledDays, freedays);
      }
    }
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    workbook.write(outputStream);
    workbook.close();
    return new ByteArrayInputStream(outputStream.toByteArray());
  }

  private void createSheet(Workbook workbook, int year, int monthNumber, List<Scheduledday> scheduledDays, List<FreeDay> freedays) {
    LocalDate currentDate = LocalDate.of(year, monthNumber, 1);
    Sheet sheet = workbook.createSheet(currentDate.getMonth().toString() + " " + year);

    // header row with classroom numbers
    Row header = sheet.createRow(0);
    for (int i = 1; i <= 6; i++) {
      Cell cell = header.createCell(i);
      cell.setCellValue("Lokaal " + i);
    }
    int monthLength = currentDate.lengthOfMonth();
    
    for (int i = 1; i <= monthLength; i++, currentDate = currentDate.plusDays(1)) {
      Row row = sheet.createRow(i);
      sheet.setColumnWidth(0, 15 * 256);

      Cell cell = row.createCell(0);
      cell.setCellValue(currentDate.toString());
      LocalDate finalCurrentDate = currentDate;
      if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY
          || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
        styleDayOff(workbook, row, cell, 181, 230, 162);
        continue;
      }
      if (freedays.stream().anyMatch((freeDay -> freeDay.getDate().equals(finalCurrentDate)))) {
        styleDayOff(workbook, row, cell, 255, 255, 0);
        continue;
      }

      for (int j = 1; j <= 6; j++) {
        sheet.setColumnWidth(j, 25 * 256);
        Cell cell1 = row.createCell(j);

        int classroom = j;
        List<Scheduledday> scheduleddaysFiltered = scheduledDays.stream().filter(
                day -> day.getDate().equals(finalCurrentDate)
                    && day.getClassroom().getId() == classroom)
            .toList();
        if (!scheduleddaysFiltered.isEmpty()) {
          Group group = scheduleddaysFiltered.getFirst().getLesson().getGroup();
          cell1.setCellValue(String.format("Group %d %s", group.getGroupNumber(), group.getField()));
          String hexColour = group.getColor();
          CellStyle cellStyle = createColor(workbook, ColorUtils.hexToRgb(hexColour));
          cell1.setCellStyle(cellStyle);
        }
        scheduledDays.removeAll(scheduleddaysFiltered);
      }
    }
  }

  private void styleDayOff(Workbook workbook, Row row, Cell cell, int r, int g, int b){
    CellStyle color = createColor(workbook, r, g, b);
    cell.setCellStyle(color);
    for (int k = 1; k <= 6; k++) {
      Cell cell1 = row.createCell(k);
      cell1.setCellStyle(color);
    }
  }

  private CellStyle createColor(Workbook workbook, int[] rgb){
    if (rgb.length < 3) return null;
    return createColor(workbook, rgb[0], rgb[1], rgb[2]);
  }
  
  private CellStyle createColor(Workbook workbook, int r, int g, int b){
    CellStyle styleColor = workbook.createCellStyle();
    styleColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    org.apache.poi.ss.usermodel.Color color = new XSSFColor(
        new java.awt.Color(r, g, b), new DefaultIndexedColorMap());
    styleColor.setFillForegroundColor(color);
    if (ColorUtils.calculateBrightness(r, g, b)) {
      Font font = workbook.createFont();
      font.setColor(IndexedColors.WHITE.getIndex());
      styleColor.setFont(font);
    }
    return styleColor;
  }
}

class OverrideException extends Exception {
  public OverrideException(String msg) {
    super(msg);
  }
}

