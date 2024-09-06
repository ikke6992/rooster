package nl.itvitae.rooster.scheduledday;

import lombok.AllArgsConstructor;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin("http://localhost:4200")
@AllArgsConstructor
@RequestMapping("api/v1/scheduleddays")
public class ScheduleddayController {

  private final ScheduleddayRepository scheduleddayRepository;
  private final ScheduleddayService scheduleddayService;
  private final ClassroomService classroomService;

  @GetMapping
  public ResponseEntity<?> getAll() {
    return ResponseEntity.ok(scheduleddayService.findAll().stream().map(ScheduleddayDTO::new).toList());
  }

  @GetMapping("/month/{month}/{year}")
  public ResponseEntity<?> getAllByMonth(@PathVariable int month, @PathVariable int year){
    return ResponseEntity.ok(scheduleddayService.findAllByMonth(month, year).stream().map(ScheduleddayDTO::new).toList());
  }

  @PutMapping("/override/{id}")
  public ResponseEntity<?> overrideScheduling(@PathVariable long id, @RequestBody OverrideRequest overrideRequest) {
    Scheduledday scheduledday = scheduleddayService.findById(id);
    LocalDate date = LocalDate.parse(overrideRequest.date());
    Classroom classroom = classroomService.getById(overrideRequest.classroomId()).get();
    if (scheduleddayRepository.existsByDateAndClassroom(date, classroom)) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(scheduleddayService.overrideScheduling(scheduledday, date, classroom, overrideRequest.adaptWeekly()));
  }
}
