package nl.itvitae.rooster.freeday;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping
  public ResponseEntity<?> addFreeDay(@RequestBody FreeDay freeDay, UriComponentsBuilder ucb){
    if (freeDayRepository.existsByDate(freeDay.getDate())) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    URI locationOfFreeDay = ucb.path("/api/v1/groups").buildAndExpand(freeDay.getId()).toUri();
    return ResponseEntity.created(locationOfFreeDay).body(freeDayRepository.save(freeDay));
  }
}
