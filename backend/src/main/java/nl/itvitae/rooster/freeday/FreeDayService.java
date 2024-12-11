package nl.itvitae.rooster.freeday;

import java.util.List;
import lombok.AllArgsConstructor;
import nl.itvitae.rooster.lesson.Lesson;
import nl.itvitae.rooster.lesson.LessonRepository;
import nl.itvitae.rooster.scheduledday.Scheduledday;
import nl.itvitae.rooster.scheduledday.ScheduleddayRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FreeDayService {
  private final FreeDayRepository freeDayRepository;
  private final ScheduleddayRepository scheduleddayRepository;
  private final LessonRepository lessonRepository;

  public FreeDay addFreeDay(FreeDay freeDay){
    List<Scheduledday> plannedOnFreeday = scheduleddayRepository.findByDate(freeDay.getDate());
    for (Scheduledday scheduledday : plannedOnFreeday) {
      Lesson lesson = scheduledday.getLesson();
      lesson.setScheduledday(null);
      lessonRepository.save(lesson);
      scheduleddayRepository.delete(scheduledday);
      lessonRepository.delete(scheduledday.getLesson());
    }
    return freeDayRepository.save(freeDay);
  }

}
