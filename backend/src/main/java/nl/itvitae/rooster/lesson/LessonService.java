package nl.itvitae.rooster.lesson;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import nl.itvitae.rooster.group.Group;
import nl.itvitae.rooster.teacher.Teacher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LessonService {
  private final LessonRepository lessonRepository;

  public List<Lesson> findAll() {
    return lessonRepository.findAll();
  }

  public Lesson createLesson(Group group){
    return lessonRepository.save(new Lesson(group));
  }

  public Lesson createLesson(Group group, Teacher teacher){
    return lessonRepository.save(new Lesson(group, teacher));
  }
}
