package nl.itvitae.rooster.freeday;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.scheduledday.ScheduleddayDTO;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/freedays")
public class FreeDayController {

  private final FreeDayRepository freeDayRepository;

  @GetMapping
  public ResponseEntity<List<FreeDay>> getAll() {
    return ResponseEntity.ok(freeDayRepository.findAllByOrderByDateAsc());
  }

  @GetMapping("/month/{month}/{year}")
  public ResponseEntity<?> getAllByMonth(@PathVariable int month, @PathVariable int year) {
    LocalDate startDate = LocalDate.of(year, month, 1);
    LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

    return ResponseEntity.ok(freeDayRepository.findByDateBetween(startDate, endDate));
  }

  @PostMapping
  public ResponseEntity<?> addFreeDay(@RequestBody FreeDay freeDay, UriComponentsBuilder ucb) {
    if (freeDayRepository.existsByDate(freeDay.getDate())) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    URI locationOfFreeDay = ucb.path("/api/v1/groups").buildAndExpand(freeDay.getId()).toUri();
    return ResponseEntity.created(locationOfFreeDay).body(freeDayRepository.save(freeDay));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> removeFreeDay(@PathVariable Long id) {
    Optional<FreeDay> optFreeDay = freeDayRepository.findById(id);
    if (optFreeDay.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    freeDayRepository.delete(optFreeDay.get());
    return ResponseEntity.noContent().build();
  }
}
