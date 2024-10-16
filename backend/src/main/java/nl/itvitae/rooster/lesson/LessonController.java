package nl.itvitae.rooster.lesson;

import java.util.Optional;
import lombok.AllArgsConstructor;
import nl.itvitae.rooster.field.Field;
import nl.itvitae.rooster.scheduledday.Scheduledday;
import nl.itvitae.rooster.scheduledday.ScheduleddayRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:4200")
@AllArgsConstructor
@RequestMapping("api/v1/lessons")
public class LessonController {

  private final LessonService lessonService;
  private final LessonRepository lessonRepository;
  private final ScheduleddayRepository scheduleddayRepository;

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
  public ResponseEntity<?> addNote(@PathVariable Long id, @RequestBody String note) {
    Optional<Scheduledday> scheduledday = scheduleddayRepository.findById(id);
    if (scheduledday.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson with Id: " + id + " does not exist");
    }
    Lesson lesson = scheduledday.get().getLesson();
    lesson.setNote(note);
    return ResponseEntity.ok(lessonRepository.save(lesson));
  }
}
