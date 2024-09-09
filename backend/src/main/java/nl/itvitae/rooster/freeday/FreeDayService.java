package nl.itvitae.rooster.freeday;

import java.util.List;
import lombok.AllArgsConstructor;
import nl.itvitae.rooster.scheduledday.Scheduledday;
import nl.itvitae.rooster.scheduledday.ScheduleddayRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FreeDayService {
  private final FreeDayRepository freeDayRepository;
  private final ScheduleddayRepository scheduleddayRepository;

  public FreeDay addFreeDay(FreeDay freeDay){
    List<Scheduledday> plannedOnFreeday = scheduleddayRepository.findByDate(freeDay.getDate());
    scheduleddayRepository.deleteAll(plannedOnFreeday);
    return freeDayRepository.save(freeDay);
  }

}
