package nl.itvitae.rooster.group.vacation;

import nl.itvitae.rooster.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long> {

  List<Vacation> findByGroup(Group group);
}
