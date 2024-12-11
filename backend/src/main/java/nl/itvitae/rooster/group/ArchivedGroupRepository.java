package nl.itvitae.rooster.group;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivedGroupRepository extends JpaRepository<ArchivedGroup, Long> {
  Optional<ArchivedGroup> findByGroupNumber(int groupNumber);
}
