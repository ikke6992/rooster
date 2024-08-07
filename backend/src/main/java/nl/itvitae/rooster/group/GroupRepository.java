package nl.itvitae.rooster.group;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {
  Optional<Group> findByGroupNumber(int groupNumber);
}
