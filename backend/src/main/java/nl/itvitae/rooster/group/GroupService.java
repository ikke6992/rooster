package nl.itvitae.rooster.group;

import java.time.DayOfWeek;
import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomService;
import nl.itvitae.rooster.field.Field;
import nl.itvitae.rooster.freeday.FreeDayRepository;
import nl.itvitae.rooster.group.vacation.Vacation;
import nl.itvitae.rooster.group.vacation.VacationRepository;
import nl.itvitae.rooster.lesson.Lesson;
import nl.itvitae.rooster.lesson.LessonRepository;
import nl.itvitae.rooster.lesson.LessonService;
import nl.itvitae.rooster.scheduledday.Scheduledday;
import nl.itvitae.rooster.scheduledday.ScheduleddayRepository;
import nl.itvitae.rooster.scheduledday.ScheduleddayService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

  private final GroupRepository groupRepository;
  private final ScheduleddayService scheduleddayService;
  private final ScheduleddayRepository scheduleddayRepository;
  private final LessonService lessonService;
  private final LessonRepository lessonRepository;
  private final ClassroomService classroomService;
  private final FreeDayRepository freeDayRepository;
  private final VacationRepository vacationRepository;

  private static final int COLOR_DISTANCE_THRESHOLD = 50;

  public List<Group> getAll() {
    return groupRepository.findAll();
  }

  public Group addGroup(int groupNumber, String color, int numberOfStudents, Field field,
      LocalDate startDate, int weeksPhase1, int weeksPhase2, int weeksPhase3) {
    return groupRepository.save(
        new Group(groupNumber, color, numberOfStudents, field, startDate, weeksPhase1, weeksPhase2,
            weeksPhase3));
  }

  public Group addVacation(Group group, LocalDate startDate, int weeks) {
    Vacation vacation = new Vacation(startDate, weeks, group);
    vacationRepository.save(vacation);
    group.addVacation(vacation);
    groupRepository.save(group);
    rescheduleGroup(group, startDate);
    return group;
  }

  public boolean checkSimilarColor(String hexColor) {
    int hexR = Integer.valueOf(hexColor.substring(1, 3), 16);
    int hexG = Integer.valueOf(hexColor.substring(3, 5), 16);
    int hexB = Integer.valueOf(hexColor.substring(5, 7), 16);
    List<Group> groups = getAll();
    for (Group group : groups) {
      int hexGroupR = Integer.valueOf(group.getColor().substring(1, 3), 16);
      int hexGroupG = Integer.valueOf(group.getColor().substring(3, 5), 16);
      int hexGroupB = Integer.valueOf(group.getColor().substring(5, 7), 16);
      double distance = Math.sqrt(
          Math.pow((hexGroupR - hexR), 2) + Math.pow((hexGroupG - hexG), 2) + Math.pow(
              (hexGroupB - hexB), 2));
      if (distance < COLOR_DISTANCE_THRESHOLD) {
        return true;
      }
    }
    return false;
  }

  public void scheduleReturnDay(Group group) {
    LocalDate date = group.getStartDate();
    for (int i = 0; i < (group.getWeeksPhase1() + group.getWeeksPhase2() + group.getWeeksPhase3()); i++) {
      if (freeDayRepository.existsByDate(date)) continue;
      scheduleddayService.addScheduledday(date.plusWeeks(i), classroomService.getById(4L).get(), lessonService.createLesson(group, true));
    }
  }

  public Group rescheduleReturnDay (Group group) {
    group.setStartDate(group.getStartDate().plusWeeks(group.getWeeksPhase1() + group.getWeeksPhase2() + group.getWeeksPhase3()));
    scheduleReturnDay(group);
    return group;
  }

  public void scheduleGroup(Group group) {
    // could be improved with lambdas, passing functions etc
    schedulePeriod(group.getWeeksPhase1(), group.getField().getDaysPhase1(), group.getStartDate(),
        group);
    schedulePeriod(group.getWeeksPhase2(), group.getField().getDaysPhase2(),
        group.getStartDate().plusWeeks(group.getWeeksPhase1()), group);
    schedulePeriod(group.getWeeksPhase3(), group.getField().getDaysPhase3(),
        group.getStartDate().plusWeeks(group.getWeeksPhase1() + group.getWeeksPhase2()), group);
  }

  public Group rescheduleGroup(Group group, LocalDate startDate) {
    // delete old scheduling
    for (Scheduledday scheduledday : scheduleddayRepository.findByLessonGroup(group)) {
      if (!scheduledday.getDate().isBefore(startDate)) {
        scheduleddayRepository.delete(scheduledday);
        lessonRepository.delete(scheduledday.getLesson());
      }
    }

    //reschedule
    int weeks = (int)ChronoUnit.WEEKS.between(group.getStartDate(), startDate);
    for (Vacation vacation : group.getVacations()) {
      if (vacation.getStartDate().isBefore(startDate)) {
        weeks -= vacation.getWeeks();
      }
    }
    LocalDate previousStartDate = startDate;
    LocalDate nextStartDate = schedulePeriod(Math.max(group.getWeeksPhase1() - weeks, 0), group.getField().getDaysPhase1(), startDate, group);
    if (nextStartDate.equals(previousStartDate)) {
      nextStartDate = schedulePeriod(Math.max(group.getWeeksPhase2() - (weeks - group.getWeeksPhase1()), 0), group.getField().getDaysPhase2(), nextStartDate, group);
    } else {
      previousStartDate = nextStartDate;
      nextStartDate = schedulePeriod(group.getWeeksPhase2(), group.getField().getDaysPhase2(), nextStartDate, group);
    }
    if (nextStartDate.equals(previousStartDate)) {
      schedulePeriod(Math.max(group.getWeeksPhase3() - (weeks - group.getWeeksPhase1() - group.getWeeksPhase2()), 0), group.getField().getDaysPhase3(), nextStartDate, group);
    } else {
      schedulePeriod(group.getWeeksPhase3(), group.getField().getDaysPhase3(), nextStartDate, group);
    }
    return group;
  }

  private LocalDate schedulePeriod(int weeksPhase, int daysPhase, LocalDate startDate, Group group) {
    int[] classroomIDs = new int[daysPhase];
    boolean isPracticum = false;

    for (int i = 1; i <= weeksPhase; i++) {
      vacation: for (int j = 1; j <= daysPhase; j++) {
        LocalDate date = startDate.plusWeeks(i - 1).plusDays(j - 1);

        // prevents scheduling vacations
        for (Vacation vacation : group.getVacations()) {
          if ((date.isAfter(vacation.getStartDate()) || date.isEqual(vacation.getStartDate())) &&
              date.isBefore(vacation.getStartDate().plusWeeks(vacation.getWeeks()))) {
            weeksPhase += 1;
            break vacation;
          }
        }
        // prevents scheduling weekends
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
          date = date.plusDays(2);
        }
        if (freeDayRepository.existsByDate(date)) continue;
        isPracticum = isPracticum || (j <= daysPhase / 2);
        final Lesson lesson = lessonService.createLesson(group, isPracticum);
        Classroom classroom = classroomService.getById(
            classroomIDs[j - 1] != 0 ? classroomIDs[j - 1] : (lesson.isPracticum() ? 4 : 1)).get();
        Scheduledday scheduledday = scheduleddayService.addScheduledday(date,
            classroom, lesson);
        if (freeDayRepository.existsByDate(scheduledday.getDate())) {
          scheduleddayRepository.delete(scheduledday);
          lessonRepository.delete(scheduledday.getLesson());
        }

        // keeps classrooms consistent
        classroomIDs[j - 1] = scheduledday.getClassroom().getId().intValue();

        // checks if the day was supposed to be a practicum day, and force the day to be practicum
        // if the current day couldnt be due to full/occupied classrooms
        isPracticum = isPracticum != scheduledday.getLesson().isPracticum();
      }
    }
    return startDate.plusWeeks(weeksPhase);
  }
}
