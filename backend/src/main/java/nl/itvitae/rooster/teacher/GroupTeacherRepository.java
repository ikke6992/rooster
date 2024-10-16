package nl.itvitae.rooster.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupTeacherRepository extends JpaRepository<GroupTeacher, Long> {
}
