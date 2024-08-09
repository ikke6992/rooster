package nl.itvitae.rooster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyDayRepository extends JpaRepository<MyDay, Long> {
}
