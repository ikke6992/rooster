package nl.itvitae.rooster.lesson;

import java.util.Optional;
import lombok.AllArgsConstructor;
import nl.itvitae.rooster.lesson.note.Note;
import nl.itvitae.rooster.lesson.note.NoteRepository;
import nl.itvitae.rooster.lesson.note.NoteRequest;
import nl.itvitae.rooster.scheduledday.Scheduledday;
import nl.itvitae.rooster.scheduledday.ScheduleddayRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:4200")
@AllArgsConstructor
@RequestMapping("api/v1/lessons")
public class LessonController {

  private final LessonService lessonService;
  private final LessonRepository lessonRepository;
  private final ScheduleddayRepository scheduleddayRepository;
  private final NoteRepository noteRepository;

  @GetMapping
  public ResponseEntity<?> getAll() {
    return ResponseEntity.ok(lessonService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    Optional<Lesson> lesson = lessonRepository.findById(id);
    if (lesson.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson with Id: " + id + " does not exist");
    }
    return ResponseEntity.ok(lesson.get());
  }

  @PutMapping("/note/{id}")
  public ResponseEntity<?> addNote(@PathVariable Long id, @RequestBody NoteRequest noteRequest) {
    Optional<Scheduledday> scheduledday = scheduleddayRepository.findById(id);
    if (scheduledday.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Scheduled day with Id: " + id + " does not exist");
    }
    Lesson lesson = scheduledday.get().getLesson();
    Optional<Note> existingNote = noteRepository.findByLesson(lesson);
    existingNote.ifPresent(noteRepository::delete);
    if (noteRequest.message() != null) {
      noteRepository.save(new Note(noteRequest.message(), lesson));
    }
    lesson.setExam(noteRequest.isExam());
    return ResponseEntity.ok(lessonRepository.save(lesson).getNote());
  }
}
