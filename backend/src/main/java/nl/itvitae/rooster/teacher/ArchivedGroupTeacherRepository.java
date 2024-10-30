package nl.itvitae.rooster.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivedGroupTeacherRepository extends JpaRepository<ArchivedGroupTeacher, Long> {
}