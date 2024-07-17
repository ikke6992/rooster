package nl.itvitae.rooster;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomRepository;
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

  @Override
  public void run(String... args) throws Exception {
    var classroom1 = saveClassroom(12, true, false);
    var classroom2 = saveClassroom(20, true, false);
    var classroom3 = saveClassroom(20, true, false);
    var classroom4 = saveClassroom(12, false, true);
    var classroom5 = saveClassroom(25, true, false);
    var classroom6 = saveClassroom(14, true, true);

    var lesson1 = saveLesson(false);
    var lesson2 = saveLesson(false);
    var lesson3 = saveLesson(true);
    var lesson4 = saveLesson(false);
    var lesson5 = saveLesson(false);
    var lesson6 = saveLesson(false);
    var lesson7 = saveLesson(false);

    saveScheduledday(LocalDate.now(), classroom6, lesson1);
    saveScheduledday(LocalDate.now(), classroom5, lesson2);
    saveScheduledday(LocalDate.now(), classroom4, lesson3);
    saveScheduledday(LocalDate.now(), classroom3, lesson4);
    saveScheduledday(LocalDate.now(), classroom2, lesson5);
    saveScheduledday(LocalDate.now(), classroom1, lesson6);
    saveScheduledday(LocalDate.now().plusDays(1), classroom4, lesson7);

    if (groupRepository.count() == 0) {
      groupRepository.save(new Group(53, "#ffa500", 10));
    }
  }

  private Classroom saveClassroom(int capacity, boolean hasBeamer, boolean forPracticum){
    return classroomRepository.save(new Classroom(capacity, hasBeamer, forPracticum));
  }

  private Lesson saveLesson(boolean isPracticum){
    return lessonRepository.save(new Lesson(isPracticum));
  }

  private Scheduledday saveScheduledday(LocalDate date, Classroom classroom, Lesson lesson){
    return scheduleddayRepository.save(new Scheduledday(date, classroom, lesson));
  }
}
