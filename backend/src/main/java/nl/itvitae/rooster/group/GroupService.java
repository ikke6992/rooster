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

  public Vacation addVacation(Group group, LocalDate startDate, int weeks) {
    Vacation vacation = new Vacation(startDate, weeks, group);
    vacationRepository.save(vacation);
    group.addVacation(vacation);
    groupRepository.save(group);
    rescheduleGroup(group, startDate);
    return vacation;
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

  public void scheduleGroup(Group group) {
    // could be improved with lambdas, passing functions etc
    schedulePeriod(group.getWeeksPhase1(), group.getField().getDaysPhase1(), group.getStartDate(),
        group);
    schedulePeriod(group.getWeeksPhase2(), group.getField().getDaysPhase2(),
        group.getStartDate().plusWeeks(group.getWeeksPhase1()), group);
    schedulePeriod(group.getWeeksPhase3(), group.getField().getDaysPhase3(),
        group.getStartDate().plusWeeks(group.getWeeksPhase1() + group.getWeeksPhase2()), group);
  }

  public void rescheduleGroup(Group group, LocalDate startDate) {
    int weeks = (int)ChronoUnit.WEEKS.between(group.getStartDate(), startDate);
    int weeksLeftPhase1 = group.getWeeksPhase1();
    int weeksLeftPhase2 = group.getWeeksPhase2();
    int weeksLeftPhase3 = group.getWeeksPhase3();

    for (Scheduledday scheduledday : scheduleddayRepository.findByLessonGroup(group)) {
      if (!scheduledday.getDate().isBefore(startDate)) {
        scheduleddayRepository.delete(scheduledday);
        lessonRepository.delete(scheduledday.getLesson());
      }
    }

    if (weeks < weeksLeftPhase1) {
      weeksLeftPhase1 -= weeks;
      schedulePeriod(weeksLeftPhase1, group.getField().getDaysPhase1(), startDate, group);
      schedulePeriod(weeksLeftPhase2, group.getField().getDaysPhase2(), startDate.plusWeeks(weeksLeftPhase1), group);
      schedulePeriod(weeksLeftPhase3, group.getField().getDaysPhase3(), startDate.plusWeeks(weeksLeftPhase1+weeksLeftPhase2), group);
    } else if (weeks < weeksLeftPhase1 + weeksLeftPhase2) {
      weeksLeftPhase2 -= (weeks + weeksLeftPhase1);
      schedulePeriod(weeksLeftPhase2, group.getField().getDaysPhase2(), startDate, group);
      schedulePeriod(weeksLeftPhase3, group.getField().getDaysPhase3(), startDate.plusWeeks(weeksLeftPhase2), group);
    } else if (weeks < weeksLeftPhase1 + weeksLeftPhase2 + weeksLeftPhase3) {
      weeksLeftPhase3 -= (weeks + weeksLeftPhase1 + weeksLeftPhase2);
      schedulePeriod(weeksLeftPhase3, group.getField().getDaysPhase3(), startDate, group);
    }
  }

  private void schedulePeriod(int weeksPhase, int daysPhase, LocalDate startDate, Group group) {
    int[] classroomIDs = new int[daysPhase];
    boolean isPracticum = false;

    for (int i = 1; i <= weeksPhase; i++) {
      for (int j = 1; j <= daysPhase; j++) {
        LocalDate date = startDate.plusWeeks(i - 1).plusDays(j - 1);

        // prevents scheduling vacations
        for (Vacation vacation : group.getVacations()) {
          if ((date.isAfter(vacation.getStartDate()) || date.isEqual(vacation.getStartDate())) &&
              date.isBefore(vacation.getStartDate().plusWeeks(vacation.getWeeks()))) {
            weeksPhase += 1;
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

        // keeps classrooms consistent
        classroomIDs[j - 1] = scheduledday.getClassroom().getId().intValue();

        // checks if the day was supposed to be a practicum day, and force the day to be practicum
        // if the current day couldnt be due to full/occupied classrooms
        isPracticum = isPracticum != scheduledday.getLesson().isPracticum();
      }
    }
  }
}
