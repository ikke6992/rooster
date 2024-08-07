package nl.itvitae.rooster.scheduledday;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.lesson.Lesson;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleddayService {
  private final ScheduleddayRepository scheduleddayRepository;

  public List<Scheduledday> findAll() {
    return scheduleddayRepository.findAll();
  }

  public List<Scheduledday> findAllByMonth(int month, int year) {
    LocalDate startDate = LocalDate.of(year, month, 1);
    LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

    return scheduleddayRepository.findByDateBetween(startDate, endDate);
  }

  public Scheduledday addScheduledday(LocalDate startdate, Classroom classroom, Lesson lesson){
    return scheduleddayRepository.save(new Scheduledday(startdate, classroom, lesson));
  }
}