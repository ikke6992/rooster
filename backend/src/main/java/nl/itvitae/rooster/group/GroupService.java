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
    for (int i = 1; i <= group.getWeeksPhase1(); i++) {

      for (int j = 1; j <= group.getField().getDaysPhase1(); j++) {
        LocalDate date = group.getStartDate().plusWeeks(i - 1).plusDays(j - 1);
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
