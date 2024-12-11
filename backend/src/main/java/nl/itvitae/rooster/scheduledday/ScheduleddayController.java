package nl.itvitae.rooster.scheduledday;

import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomService;
import nl.itvitae.rooster.freeday.FreeDayRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:4200")
@AllArgsConstructor
@RequestMapping("api/v1/scheduleddays")
public class ScheduleddayController {

  private final ScheduleddayRepository scheduleddayRepository;
  private final ScheduleddayService scheduleddayService;
  private final ClassroomService classroomService;
  private final FreeDayRepository freeDayRepository;

  @GetMapping
  public ResponseEntity<?> getAll() {
    return ResponseEntity.ok(
        scheduleddayService.findAll().stream().map(ScheduleddayDTO::new).toList());
  }

  @GetMapping("/month/{month}/{year}")
  public ResponseEntity<?> getAllByMonth(@PathVariable int month, @PathVariable int year) {
    return ResponseEntity.ok(
        scheduleddayService.findAllByMonth(month, year).stream().map(ScheduleddayDTO::new)
            .toList());
  }

  @GetMapping("/export")
  public ResponseEntity<?> exportExcel() throws IOException {
    var body = scheduleddayService.createExcel();
    if (body == null) {
      return ResponseEntity.badRequest().body("No days planned for this year");
    }
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "attachment; filename=scheduleddays.xlsx");

    return ResponseEntity.ok().headers(headers)
        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
        .body(new InputStreamResource(body));
  }

  @PutMapping("/override/{id}")
  public ResponseEntity<?> overrideScheduling(@PathVariable long id, @RequestBody OverrideRequest overrideRequest) {
    Scheduledday scheduledday = scheduleddayService.findById(id);
    LocalDate date = LocalDate.parse(overrideRequest.date());
    Optional<Classroom> classroom = classroomService.getById(overrideRequest.classroomId());
    if (classroom.isEmpty()) {
      return ResponseEntity.badRequest().body("Classroom does not exist");
    }
    if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)
        || freeDayRepository.existsByDate(date)){
      return ResponseEntity.badRequest().body("Day cannot be scheduled on weekends on freedays");
    }
    if (scheduleddayRepository.existsByDateAndClassroom(date, classroom.get())
        || (!date.equals(scheduledday.getDate()) && scheduleddayRepository.existsByDateAndLessonGroup(
            date, scheduledday.getLesson().getGroup()))) {
      return ResponseEntity.badRequest().body("Day + Classroom are already scheduled");
    }
    return ResponseEntity.ok(scheduleddayService.overrideScheduling(
        scheduledday, date, classroom.get(), overrideRequest.adaptWeekly()));
  }
}
