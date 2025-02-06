package nl.itvitae.rooster.classroom;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/classrooms")
public class ClassroomController {

  private final ClassroomService lessonService;

  @GetMapping
  public ResponseEntity<?> getAll() {
    return ResponseEntity.ok(lessonService.findAll());
  }

}
