package nl.itvitae.rooster;

import lombok.AllArgsConstructor;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomRepository;
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

  @Override
  public void run(String... args) throws Exception {
    var classroom = saveClassroom(12, true, false);
    var lesson = saveLesson(false);
    saveScheduledday(classroom, lesson);
  }

  private Classroom saveClassroom(int capacity, boolean hasBeamer, boolean forPracticum){
    return classroomRepository.save(new Classroom(capacity, hasBeamer, forPracticum));
  }

  private Lesson saveLesson(boolean isPracticum){
    return lessonRepository.save(new Lesson(isPracticum));
  }

  private Scheduledday saveScheduledday(Classroom classroom, Lesson lesson){
    return scheduleddayRepository.save(new Scheduledday(classroom, lesson));
  }
}
