package nl.itvitae.rooster.scheduledday;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:4200")
@AllArgsConstructor
@RequestMapping("api/v1/scheduleddays")
public class ScheduleddayController {

  private final ScheduleddayService scheduleddayService;

  @GetMapping
  public ResponseEntity<?> getAll() {
    return ResponseEntity.ok(scheduleddayService.findAll().stream().map(ScheduleddayDTO::new).toList());
  }

  @GetMapping("/month/{month}/{year}")
  public ResponseEntity<?> getAllByMonth(@PathVariable int month, @PathVariable int year){
    return ResponseEntity.ok(scheduleddayService.findAllByMonth(month, year).stream().map(ScheduleddayDTO::new).toList());
  }
}
