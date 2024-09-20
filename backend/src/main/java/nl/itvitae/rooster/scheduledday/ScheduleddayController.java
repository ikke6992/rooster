package nl.itvitae.rooster.scheduledday;

import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    return ResponseEntity.ok(
        scheduleddayService.findAll().stream().map(ScheduleddayDTO::new).toList());
  }

  @GetMapping("/month/{month}/{year}")
  public ResponseEntity<?> getAllByMonth(@PathVariable int month, @PathVariable int year) {
    return ResponseEntity.ok(
        scheduleddayService.findAllByMonth(month, year).stream().map(ScheduleddayDTO::new)
            .toList());
  }

  @GetMapping("/export/{year}")
  public ResponseEntity<?> exportExcel(@PathVariable int year) throws IOException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "attachment; filename=scheduleddays.xlsx");
    var body = scheduleddayService.createExcel(year);

    return ResponseEntity.ok().headers(headers)
        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
        .body(new InputStreamResource(body));
  }
}
