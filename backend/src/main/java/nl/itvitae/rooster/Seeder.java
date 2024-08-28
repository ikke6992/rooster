package nl.itvitae.rooster;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomRepository;
import nl.itvitae.rooster.field.Field;
import nl.itvitae.rooster.field.FieldRepository;
import nl.itvitae.rooster.group.Group;
import nl.itvitae.rooster.group.GroupRepository;
import nl.itvitae.rooster.group.GroupService;
import nl.itvitae.rooster.teacher.Teacher;
import nl.itvitae.rooster.teacher.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

  private final MyDayRepository myDayRepository;
  private final ClassroomRepository classroomRepository;
  private final GroupRepository groupRepository;
  private final GroupService groupService;
  private final FieldRepository fieldRepository;
  private final TeacherRepository teacherRepository;

  @Override
  public void run(String... args) throws Exception {
    var monday = saveDay(DayOfWeek.MONDAY);
    var tuesday = saveDay(DayOfWeek.TUESDAY);
    var wednesday = saveDay(DayOfWeek.WEDNESDAY);
    var thursday = saveDay(DayOfWeek.THURSDAY);
    var friday = saveDay(DayOfWeek.FRIDAY);

    var classroom1 = saveClassroom(12, true, false);
    var classroom2 = saveClassroom(20, true, false);
    var classroom3 = saveClassroom(20, true, false);
    var classroom4 = saveClassroom(12, false, true);
    var classroom5 = saveClassroom(25, true, false);
    var classroom6 = saveClassroom(14, true, true);

    var java = saveField("Java", 3, 4, 2);
    var data = saveField("Data", 3, 4, 3);
    var cloud = saveField("Cloud", 2, 4, 2);
    var security = saveField("Security", 3, 3, 2);

    var group53 = saveGroup(53, "#ffa500", 12, java);

    var wubbo = saveTeacher("Wubbo", true, new ArrayList<>(List.of(monday, tuesday, wednesday, friday)), 3, group53);
    var coen = saveTeacher("Coen", false, new ArrayList<>(List.of(monday, thursday)), 2, group53);

    groupService.scheduleGroup(group53);
  }

  private Group saveGroup(int groupNumber, String color,int numberOfStudents, Field field) {
    Group group = groupService.addGroup(groupNumber, color, numberOfStudents, field, LocalDate.now(), 8, 12, 8);
    return group;
  }

  private Teacher saveTeacher(String name, boolean teachesPracticum, List<MyDay> availability, int maxDaysPerWeek, Group... groups) {
    Teacher teacher = new Teacher(name, teachesPracticum, availability, maxDaysPerWeek);
    for (Group group : groups) {
      teacher.addGroup(group);
      group.addTeacher(teacher);
    }
    return teacherRepository.save(teacher);
  }

  private MyDay saveDay(DayOfWeek day) {
    return myDayRepository.save(new MyDay(day));
  }

  private Classroom saveClassroom(int capacity, boolean hasBeamer, boolean forPracticum) {
    return classroomRepository.save(new Classroom(capacity, hasBeamer, forPracticum));
  }

  private Field saveField(String name, int daysPhase1, int daysPhase2, int daysPhase3) {
    return fieldRepository.save(new Field(name, daysPhase1, daysPhase2, daysPhase3));
  }
}
