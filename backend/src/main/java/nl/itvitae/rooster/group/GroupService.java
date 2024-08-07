package nl.itvitae.rooster.group;

import java.time.DayOfWeek;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomService;
import nl.itvitae.rooster.field.Field;
import nl.itvitae.rooster.lesson.Lesson;
import nl.itvitae.rooster.lesson.LessonService;
import nl.itvitae.rooster.scheduledday.ScheduleddayService;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

  private final GroupRepository groupRepository;
  private final ScheduleddayService scheduleddayService;
  private final LessonService lessonService;
  private final ClassroomService classroomService;

  public List<Group> getAll() {
    return groupRepository.findAll();
  }

  public Group addGroup(int groupNumber, String color, int numberOfStudents, Field field,
      LocalDate startDate, int weeksPhase1, int weeksPhase2, int weeksPhase3) {
    return groupRepository.save(
        new Group(groupNumber, color, numberOfStudents, field, startDate, weeksPhase1, weeksPhase2,
            weeksPhase3));
  }

  public void scheduleGroup(Group group) {
    schedulePeriod(group.getWeeksPhase1(), group.getField().getDaysPhase1(), group.getStartDate(),
        group);
    schedulePeriod(group.getWeeksPhase2(), group.getField().getDaysPhase2(),
        group.getStartDate().plusWeeks(group.getWeeksPhase1()), group);
    schedulePeriod(group.getWeeksPhase3(), group.getField().getDaysPhase3(),
        group.getStartDate().plusWeeks(group.getWeeksPhase1() + group.getWeeksPhase2()), group);
  }

  private void schedulePeriod(int weeksPhase, int daysPhase, LocalDate startDate, Group group) {
    for (int i = 1; i <= weeksPhase; i++) {
      for (int j = 1; j <= daysPhase; j++) {
        LocalDate date = startDate.plusWeeks(i - 1).plusDays(j - 1);
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
          date = date.plusDays(2);
        } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
          date = date.plusDays(2);
        }
        final Classroom classroom = classroomService.getById(j).get();
        final Lesson lesson = lessonService.createLesson(group, j < 2);
        scheduleddayService.addScheduledday(date,
            classroom, lesson);
      }
    }
  }
}
