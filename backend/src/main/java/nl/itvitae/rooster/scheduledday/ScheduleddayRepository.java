package nl.itvitae.rooster.scheduledday;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleddayRepository extends JpaRepository<Scheduledday, Long> {
  List<Scheduledday> findByDateBetween(LocalDate startDate, LocalDate endDate);

}
