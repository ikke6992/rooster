package nl.itvitae.rooster;

import java.time.LocalDate;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

  private final ClassroomRepository classroomRepository;
  private final LessonRepository lessonRepository;
  private final ScheduleddayRepository scheduleddayRepository;
  private final GroupRepository groupRepository;
  private final FieldRepository fieldRepository;

  @Override
  public void run(String... args) throws Exception {
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

  private Classroom saveClassroom(int capacity, boolean hasBeamer, boolean forPracticum) {
    return classroomRepository.save(new Classroom(capacity, hasBeamer, forPracticum));
  }

  private Lesson saveLesson(Group group, boolean isPracticum) {
    return lessonRepository.save(new Lesson(group, isPracticum));
  }

  private Scheduledday saveScheduledday(LocalDate date, Classroom classroom, Lesson lesson) {
    return scheduleddayRepository.save(new Scheduledday(date, classroom, lesson));
  }

  private Group saveGroup(int groupNumber, String colour,int numberOfStudents, Field field) {
    return groupRepository.save(new Group(groupNumber, colour, numberOfStudents, field, LocalDate.now(), 8, 12, 8));
  }

  private Field saveField(String name, int daysPhase1, int daysPhase2, int daysPhase3) {
    return fieldRepository.save(new Field(name, daysPhase1, daysPhase2, daysPhase3));
  }

}
