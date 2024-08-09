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
import nl.itvitae.rooster.lesson.Lesson;
import nl.itvitae.rooster.lesson.LessonRepository;
import nl.itvitae.rooster.scheduledday.Scheduledday;
import nl.itvitae.rooster.scheduledday.ScheduleddayRepository;
import nl.itvitae.rooster.teacher.Teacher;
import nl.itvitae.rooster.teacher.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

  private final MyDayRepository myDayRepository;
  private final ClassroomRepository classroomRepository;
  private final LessonRepository lessonRepository;
  private final ScheduleddayRepository scheduleddayRepository;
  private final GroupRepository groupRepository;
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

    var group53 = saveGroup(53, "#ffa500", 10, java);

    var wubbo = saveTeacher("Wubbo", new ArrayList<>(List.of(monday, tuesday, wednesday, friday)), 3, group53);
    var coen = saveTeacher("Coen", new ArrayList<>(List.of(monday, thursday)), 2, group53);

    var lesson1 = saveLesson(group53, false);
    var lesson2 = saveLesson(group53, false);
    var lesson3 = saveLesson(group53, true);
    var lesson4 = saveLesson(group53, false);
    var lesson5 = saveLesson(group53, false);
    var lesson6 = saveLesson(group53, false);
    var lesson7 = saveLesson(group53, false);

    saveScheduledday(LocalDate.now(), classroom6, lesson1);
    saveScheduledday(LocalDate.now(), classroom5, lesson2);
    saveScheduledday(LocalDate.now(), classroom4, lesson3);
    saveScheduledday(LocalDate.now(), classroom3, lesson4);
    saveScheduledday(LocalDate.now(), classroom2, lesson5);
    saveScheduledday(LocalDate.now(), classroom1, lesson6);
    saveScheduledday(LocalDate.now().plusDays(1), classroom4, lesson7);
  }

  private MyDay saveDay(DayOfWeek day) {
    return myDayRepository.save(new MyDay(day));
  }

  private Classroom saveClassroom(int capacity, boolean hasBeamer, boolean forPracticum) {
    return classroomRepository.save(new Classroom(capacity, hasBeamer, forPracticum));
  }

  private Lesson saveLesson(Group group, boolean isPracticum) {
    return lessonRepository.save(new Lesson(group, isPracticum));
  }

  private Scheduledday saveScheduledday(LocalDate date, Classroom classroom, Lesson lesson) {
    return scheduleddayRepository.save(new Scheduledday(date, classroom, lesson));
  }

  private Group saveGroup(int groupNumber, String color,int numberOfStudents, Field field) {
    return groupRepository.save(new Group(groupNumber, color, numberOfStudents, field, LocalDate.now(), 8, 12, 8));
  }

  private Field saveField(String name, int daysPhase1, int daysPhase2, int daysPhase3) {
    return fieldRepository.save(new Field(name, daysPhase1, daysPhase2, daysPhase3));
  }

  private Teacher saveTeacher(String name, List<MyDay> availability, int maxDaysPerWeek, Group... groups) {
    Teacher teacher = new Teacher(name, availability, maxDaysPerWeek);
    for (Group group : groups) {
      teacher.addGroup(group);
    }
    return teacherRepository.save(teacher);
  }

}
