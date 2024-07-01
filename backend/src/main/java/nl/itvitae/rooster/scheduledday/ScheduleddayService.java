package nl.itvitae.rooster.scheduledday;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleddayService {
  private final ScheduleddayRepository scheduleddayRepository;

  public List<Scheduledday> findAll() {
    return scheduleddayRepository.findAll();
  }

}