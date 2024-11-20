package nl.itvitae.rooster.group.vacation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivedVacationRepository extends JpaRepository<ArchivedVacation, Long> {
}
