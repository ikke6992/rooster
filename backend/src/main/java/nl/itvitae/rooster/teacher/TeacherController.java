package nl.itvitae.rooster.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/teachers")
public class TeacherController {

  private final TeacherService teacherService;

  @GetMapping
  public ResponseEntity<List<TeacherDTO>> getAll() {
    return ResponseEntity.ok(teacherService.getAll().stream().map(TeacherDTO::of).toList());
  }

  @PutMapping("/edit/{id}")
  public ResponseEntity<TeacherDTO> setAvailability(@PathVariable long id, @RequestBody AvailabilityRequest request) {
    return ResponseEntity.ok(TeacherDTO.of(teacherService.setAvailability(id, request.availability(), request.maxDaysPerWeek())));
  }
}
