package nl.itvitae.rooster.lesson;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:5173")
@AllArgsConstructor
@RequestMapping("api/v1/lessons")
public class LessonController {

  private final LessonService lessonService;

  @GetMapping
  public ResponseEntity<?> getAll() {
    return ResponseEntity.ok(lessonService.findAll());
  }
}
