package nl.itvitae.rooster.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivedLessonRepository extends JpaRepository<ArchivedLesson, Long> {
}
