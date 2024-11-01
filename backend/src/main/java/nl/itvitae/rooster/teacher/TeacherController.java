package nl.itvitae.rooster.teacher;

import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.group.Group;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
@RequestMapping("/api/v1/teachers")
public class TeacherController {

  private final TeacherService teacherService;

  @GetMapping
  public ResponseEntity<List<TeacherDTO>> getAll() {
    return ResponseEntity.ok(teacherService.getAll().stream().map(TeacherDTO::of).toList());
  }

  @PostMapping("/new")
  public ResponseEntity<?> addTeacher(@RequestBody TeacherRequest request, UriComponentsBuilder ucb) {
    if (request.availability().length == 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          "Teacher needs to be available on at least one of the five work days");
    }
    if (request.maxDaysPerWeek() < 1) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Teacher needs to work at least 1 day per week");
    }
    final Teacher teacher = teacherService.addTeacher(request.name(), request.availability(), request.maxDaysPerWeek());
    URI locationOfTeacher = ucb.path("/api/v1/teachers").buildAndExpand(teacher.getId()).toUri();
    return ResponseEntity.created(locationOfTeacher).body(teacher);
  }

  @PutMapping("/edit/{id}/addGroup/{groupNumber}")
  public ResponseEntity<?> addGroup(
      @PathVariable long id, @PathVariable int groupNumber, @RequestBody DaysAssignedRequest request) {
    if (request.daysPhase1() < 1 || request.daysPhase1() > 5 || request.daysPhase2() < 1
        || request.daysPhase2() > 5 || request.daysPhase3() < 1 || request.daysPhase3() > 5) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount of days needs to be between 1 and 5");
    }
    for (GroupTeacher groupTeacher : teacherService.getById(id).getGroupTeachers()) {
      if (groupTeacher.getGroup().getGroupNumber() == groupNumber) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Teacher is already assigned to this group.");
      }
    }
    return ResponseEntity.ok(TeacherDTO.of(teacherService.addGroup(
        id, groupNumber, request.daysPhase1(), request.daysPhase2(), request.daysPhase3())));
  }

  @PutMapping("/edit/{id}")
  public ResponseEntity<?> setAvailability(@PathVariable long id, @RequestBody AvailabilityRequest request) {
    if (request.availability().length == 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          "Teacher needs to be available on at least one of the five work days");
    }
    if (request.maxDaysPerWeek() < 1) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Teacher needs to work at least 1 day per week");
    }
    return ResponseEntity.ok(TeacherDTO.of(teacherService.setAvailability(
        id, request.availability(), request.maxDaysPerWeek())));
  }

}
