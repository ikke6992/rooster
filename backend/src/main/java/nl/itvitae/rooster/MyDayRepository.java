package nl.itvitae.rooster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;

@Repository
public interface MyDayRepository extends JpaRepository<MyDay, Long> {

  MyDay findByDay(DayOfWeek day);
}
