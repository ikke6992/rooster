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

  private static final int COLOUR_DISTANCE_THRESHOLD = 50;

  public List<Group> getAll() {
    return groupRepository.findAll();
  }

  public Group addGroup(int groupNumber, String color, int numberOfStudents, Field field,
      LocalDate startDate, int weeksPhase1, int weeksPhase2, int weeksPhase3) {
    return groupRepository.save(
        new Group(groupNumber, color, numberOfStudents, field, startDate, weeksPhase1, weeksPhase2,
            weeksPhase3));
  }

  public boolean checkSimilarColour(String hexColour){
    int hexR = Integer.valueOf(hexColour.substring(1, 3), 16);
    int hexG = Integer.valueOf(hexColour.substring(3, 5), 16);
    int hexB = Integer.valueOf(hexColour.substring(5, 7), 16);
    List<Group> groups = getAll();
    for (Group group : groups){
      int hexGroupR = Integer.valueOf(group.getColor().substring(1, 3), 16);
      int hexGroupG = Integer.valueOf(group.getColor().substring(3, 5), 16);
      int hexGroupB = Integer.valueOf(group.getColor().substring(5, 7), 16);
      double distance = Math.sqrt(Math.pow((hexGroupR - hexR), 2) + Math.pow((hexGroupG - hexG), 2) + Math.pow((hexGroupB - hexB), 2));
      if (distance < COLOUR_DISTANCE_THRESHOLD) {
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

  private void schedulePeriod(int weeksPhase, int daysPhase, LocalDate startDate, Group group) {
    // still to do
    // prevent conflicts
    // assign classrooms based on lesson type (practicum)
    // ask % practicum lessons with PO
    // optional: don't schedule all days in a row

    for (int i = 1; i <= weeksPhase; i++) {
      for (int j = 1; j <= daysPhase; j++) {
        LocalDate date = startDate.plusWeeks(i - 1).plusDays(j - 1);
        // prevents scheduling weekends
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
          date = date.plusDays(2);
        }
        final Lesson lesson = lessonService.createLesson(group, j < 2);
        final Classroom classroom = classroomService.getById(j).get();
        scheduleddayService.addScheduledday(date,
            classroom, lesson);
      }
    }
  }
}
