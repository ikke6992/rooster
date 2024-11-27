package nl.itvitae.rooster.lesson.note;

import nl.itvitae.rooster.lesson.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

  Optional<Note> findByLesson(Lesson lesson);
}
