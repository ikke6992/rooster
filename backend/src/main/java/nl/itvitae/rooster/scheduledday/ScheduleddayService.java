package nl.itvitae.rooster.scheduledday;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomRepository;
import nl.itvitae.rooster.lesson.Lesson;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleddayService {
  private final ScheduleddayRepository scheduleddayRepository;
  private final ClassroomRepository classroomRepository;

  public List<Scheduledday> findAll() {
    return scheduleddayRepository.findAll();
  }

  public List<Scheduledday> findAllByMonth(int month, int year) {
    LocalDate startDate = LocalDate.of(year, month, 1);
    LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

    return scheduleddayRepository.findByDateBetween(startDate, endDate);
  }

  public Scheduledday addScheduledday(LocalDate date, Classroom classroom, Lesson lesson){
    Scheduledday scheduledday = new Scheduledday(date, classroom, lesson);
    preventConflicts(scheduledday);
    return scheduleddayRepository.save(scheduledday);
  }

  private void preventConflicts(Scheduledday scheduledday){
    if (scheduleddayRepository.existsByDateAndClassroom(scheduledday.getDate(), scheduledday.getClassroom())) {
      scheduledday.setClassroom(classroomRepository.findById(scheduledday.getClassroom().getId() + 1).get());
      preventConflicts(scheduledday);
    }
  }
}