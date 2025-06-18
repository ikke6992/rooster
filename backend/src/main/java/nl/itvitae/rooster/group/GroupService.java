package nl.itvitae.rooster.group;

import java.time.DayOfWeek;

import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomService;
import nl.itvitae.rooster.freeday.FreeDayRepository;
import nl.itvitae.rooster.group.vacation.ArchivedVacation;
import nl.itvitae.rooster.group.vacation.ArchivedVacationRepository;
import nl.itvitae.rooster.group.vacation.Vacation;
import nl.itvitae.rooster.group.vacation.VacationRepository;
import nl.itvitae.rooster.lesson.*;
import nl.itvitae.rooster.scheduledday.*;
import nl.itvitae.rooster.teacher.*;
import nl.itvitae.rooster.utils.ColorUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

  private final ArchivedGroupRepository archivedGroupRepository;
  private final ArchivedGroupTeacherRepository archivedGroupTeacherRepository;
  private final ArchivedLessonRepository archivedLessonRepository;
  private final ArchivedScheduleddayRepository archivedScheduleddayRepository;
  private final ArchivedVacationRepository archivedVacationRepository;
  private final GroupRepository groupRepository;
  private final GroupTeacherRepository groupTeacherRepository;
  private final TeacherService teacherService;
  private final TeacherRepository teacherRepository;
  private final ScheduleddayService scheduleddayService;
  private final ScheduleddayRepository scheduleddayRepository;
  private final LessonService lessonService;
  private final LessonRepository lessonRepository;
  private final ClassroomService classroomService;
  private final FreeDayRepository freeDayRepository;
  private final VacationRepository vacationRepository;

  public List<Group> getAll() {
    return groupRepository.findAll();
  }

  public List<ArchivedGroup> getArchived() {
    return archivedGroupRepository.findAll();
  }

  public Group addGroup(int groupNumber, String color, int numberOfStudents, String field,
                        LocalDate startDate, int daysPhase1, int weeksPhase1,
                        int daysPhase2, int weeksPhase2, int daysPhase3, int weeksPhase3) {
    return groupRepository.save(
        new Group(groupNumber, color, numberOfStudents, field, startDate,
            daysPhase1, weeksPhase1, daysPhase2, weeksPhase2, daysPhase3, weeksPhase3));
  }

  public Group editGroup(Group group, int groupNumber, String color, int numberOfStudents, String field,
                         LocalDate startDate, int daysPhase1, int weeksPhase1,
                         int daysPhase2, int weeksPhase2, int daysPhase3, int weeksPhase3, AssignmentRequest[] teacherAssignments) {
    group.setGroupNumber(groupNumber);
    group.setColor(color);
    group.setNumberOfStudents(numberOfStudents);
    group.setField(field);
    group.setStartDate(startDate);
    group.setDaysPhase1(daysPhase1);
    group.setWeeksPhase1(weeksPhase1);
    group.setDaysPhase2(daysPhase2);
    group.setWeeksPhase2(weeksPhase2);
    group.setDaysPhase3(daysPhase3);
    group.setWeeksPhase3(weeksPhase3);

    List<GroupTeacher> groupTeachers = List.copyOf(group.getGroupTeachers());

    for (int i = 0; i < groupTeachers.size(); i++) {
      teacherService.removeGroup(groupTeachers.get(i));
    }

    for (AssignmentRequest teacherAssignment : teacherAssignments) {
      teacherService.addGroup(teacherService.getById(teacherAssignment.id()), group,
          teacherAssignment.daysPhase1(), teacherAssignment.daysPhase2(), teacherAssignment.daysPhase3());
    }

    return groupRepository.save(group);
  }

  public ArchivedGroup deleteGroup(Group group) {
    ArchivedGroup archivedGroup = new ArchivedGroup(group);
    archivedGroupRepository.save(archivedGroup);
    for (Lesson lesson : lessonRepository.findByGroup(group)) {
      ArchivedLesson archivedLesson = new ArchivedLesson(lesson, archivedGroup);
      archivedLessonRepository.save(archivedLesson);
      Scheduledday scheduledday = lesson.getScheduledday();
      archivedScheduleddayRepository.save(new ArchivedScheduledday(scheduledday, archivedLesson));
      lesson.setScheduledday(null);
      lessonRepository.save(lesson);
      scheduleddayRepository.delete(scheduledday);
      Teacher teacher = lesson.getTeacher();
      if (teacher != null) {
        teacher.removeLesson(lesson);
        teacher.addArchivedLesson(archivedLesson);
        teacherRepository.save(teacher);
      }
      group.removeLesson(lesson);
      lessonRepository.delete(lesson);
    }
    for (Vacation vacation : vacationRepository.findByGroup(group)) {
      group.removeVacation(vacation);
      vacationRepository.delete(vacation);
      archivedGroup.addVacation(archivedVacationRepository.save(new ArchivedVacation(vacation, archivedGroup)));
    }
    for (GroupTeacher groupTeacher : group.getGroupTeachers()) {
      ArchivedGroupTeacher archivedGroupTeacher = new ArchivedGroupTeacher(groupTeacher, archivedGroup);
      archivedGroupTeacherRepository.save(archivedGroupTeacher);
      groupTeacherRepository.delete(groupTeacher);
    }
    groupRepository.delete(group);
    return archivedGroupRepository.save(archivedGroup);
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
    int[] rgb = ColorUtils.hexToRgb(hexColor);
    List<String> groupColors = Stream.concat(getAll().stream().map(Group::getColor), getArchived().stream().map(ArchivedGroup::getColor)).toList();
    for (String groupColor : groupColors) {
      int[] groupRgb = ColorUtils.hexToRgb(groupColor);
      double distance = Math.sqrt(
          Math.pow((groupRgb[0] - rgb[0]), 2) + Math.pow((groupRgb[1] - rgb[1]), 2) + Math.pow(
              (groupRgb[2] - rgb[2]), 2));
      if (distance < ColorUtils.COLOR_DISTANCE_THRESHOLD) {
        return true;
      }
    }
    return false;
  }

  public void scheduleReturnDay(Group group, long classroomId, DayOfWeek dayOfWeek) {
    LocalDate date = group.getStartDate()
        .minusDays(group.getStartDate().getDayOfWeek().getValue()).plusDays(dayOfWeek.getValue());
    for (int i = 0; i < (group.getWeeksPhase1() + group.getWeeksPhase2() + group.getWeeksPhase3()); i++) {
      if (freeDayRepository.existsByDate(date)) continue;
      scheduleddayService.addScheduledday(1, date.plusWeeks(i), classroomService.getById(classroomId).get(),
          lessonService.createLesson(group));
    }
  }

  public Group rescheduleReturnDay (Group group) {
    group.setStartDate(group.getStartDate().plusWeeks(
        group.getWeeksPhase1() + group.getWeeksPhase2() + group.getWeeksPhase3()));

    List<Scheduledday> scheduledreturndays = scheduleddayRepository.findByLessonGroup(group);
    Scheduledday latestScheduledreturnday = scheduledreturndays.get(0);
    for (Scheduledday scheduledreturnday : scheduledreturndays) {
      if (scheduledreturnday.getDate().isAfter(latestScheduledreturnday.getDate())) {
        latestScheduledreturnday = scheduledreturnday;
      }
    }
    scheduleReturnDay(group, latestScheduledreturnday.getClassroom().getId(),
        latestScheduledreturnday.getDate().getDayOfWeek());
    return group;
  }

  public void scheduleGroup(Group group) {
    // could be improved with lambdas, passing functions etc
    schedulePeriod(1, group.getWeeksPhase1(), group.getDaysPhase1(), group.getStartDate(),
        group);
    schedulePeriod(2, group.getWeeksPhase2(), group.getDaysPhase2(),
        group.getStartDate().plusWeeks(group.getWeeksPhase1()), group);
    schedulePeriod(3, group.getWeeksPhase3(), group.getDaysPhase3(),
        group.getStartDate().plusWeeks(group.getWeeksPhase1() + group.getWeeksPhase2()), group);
  }

  public Group rescheduleGroup(Group group, LocalDate startDate) {
    // delete old scheduling
    for (Scheduledday scheduledday : scheduleddayRepository.findByLessonGroup(group)) {
      if (!scheduledday.getDate().isBefore(startDate)) {
        Lesson lesson = scheduledday.getLesson();
        lesson.setScheduledday(null);
        lessonRepository.save(lesson);
        scheduleddayRepository.delete(scheduledday);
        lessonRepository.delete(lesson);
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
    LocalDate nextStartDate = schedulePeriod(1, Math.max(group.getWeeksPhase1() - weeks, 0),
        group.getDaysPhase1(), startDate, group);
    if (nextStartDate.equals(previousStartDate)) {
      nextStartDate = schedulePeriod(2, Math.max(group.getWeeksPhase2() - (weeks - group.getWeeksPhase1()), 0),
          group.getDaysPhase2(), nextStartDate, group);
    } else {
      previousStartDate = nextStartDate;
      nextStartDate = schedulePeriod(2, group.getWeeksPhase2(), group.getDaysPhase2(), nextStartDate, group);
    }
    if (nextStartDate.equals(previousStartDate)) {
      schedulePeriod(3, Math.max(
          group.getWeeksPhase3() - (weeks - group.getWeeksPhase1() - group.getWeeksPhase2()), 0),
          group.getDaysPhase3(), nextStartDate, group);
    } else {
      schedulePeriod(3, group.getWeeksPhase3(), group.getDaysPhase3(), nextStartDate, group);
    }
    return group;
  }

  private LocalDate schedulePeriod(int phase, int weeksPhase, int daysPhase, LocalDate startDate, Group group) {
    int[] classroomIDs = new int[daysPhase];

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
        final Lesson lesson = lessonService.createLesson(group);
        Classroom classroom = classroomService.getById(classroomIDs[j - 1] != 0 ? classroomIDs[j - 1] : 1).get();
        Scheduledday scheduledday = scheduleddayService.addScheduledday(phase, date,
            classroom, lesson);
        if (freeDayRepository.existsByDate(scheduledday.getDate())) {
          Lesson scheduledLesson = scheduledday.getLesson();
          scheduledLesson.setScheduledday(null);
          lessonRepository.save(scheduledLesson);
          scheduleddayRepository.delete(scheduledday);
          lessonRepository.delete(scheduledLesson);
        }

        // keeps classrooms consistent
        classroomIDs[j - 1] = scheduledday.getClassroom().getId().intValue();
      }
    }
    return startDate.plusWeeks(weeksPhase);
  }
}
