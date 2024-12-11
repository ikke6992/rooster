package nl.itvitae.rooster.scheduledday;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivedScheduleddayRepository extends JpaRepository<ArchivedScheduledday, Long> {
}
