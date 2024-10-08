package nl.itvitae.rooster.freeday;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeDayRepository  extends JpaRepository<FreeDay, Long> {

  List<FreeDay> findByDateBetween(LocalDate startDate, LocalDate endDate);

  boolean existsByDate(LocalDate date);

  List<FreeDay> findAllByOrderByDateAsc();

}
