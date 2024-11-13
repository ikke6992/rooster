package nl.itvitae.rooster.lesson;

import nl.itvitae.rooster.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

  List<Lesson> findByGroup(Group group);

}
