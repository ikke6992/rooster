package nl.itvitae.rooster.lesson;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LessonService {
  private final LessonRepository lessonRepository;

  public List<Lesson> findAll() {
    return lessonRepository.findAll();
  }
}
