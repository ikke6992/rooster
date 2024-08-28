package nl.itvitae.rooster.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/teachers")
public class TeacherController {

  private final TeacherService teacherService;

  @GetMapping("/")
  public ResponseEntity<List<TeacherDTO>> getAll() {
    return ResponseEntity.ok(teacherService.getAll().stream().map(TeacherDTO::of).toList());
  }

  @PostMapping("/new")
  public ResponseEntity<?> addTeacher(@RequestBody TeacherRequest request, UriComponentsBuilder ucb) {
    final Teacher teacher = teacherService.addTeacher(request.name(), request.teachesPracticum(), request.availability(), request.maxDaysPerWeek());
    URI locationOfTeacher = ucb.path("/api/v1/teachers").buildAndExpand(teacher.getId()).toUri();
    return ResponseEntity.created(locationOfTeacher).body(teacher);
  }

  @PutMapping("/edit/{id}")
  public ResponseEntity<TeacherDTO> setAvailability(@PathVariable long id, @RequestBody AvailabilityRequest request) {
    return ResponseEntity.ok(TeacherDTO.of(teacherService.setAvailability(id, request.availability(), request.maxDaysPerWeek())));
  }
}
