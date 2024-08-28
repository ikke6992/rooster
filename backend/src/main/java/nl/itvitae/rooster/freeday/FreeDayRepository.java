package nl.itvitae.rooster.freeday;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeDayRepository  extends JpaRepository<FreeDay, Long> {

  boolean existsByDate(LocalDate date);

}
