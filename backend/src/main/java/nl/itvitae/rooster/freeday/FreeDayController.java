package nl.itvitae.rooster.freeday;

import static de.focus_shift.jollyday.core.HolidayCalendar.NETHERLANDS;

import de.focus_shift.jollyday.core.Holiday;
import de.focus_shift.jollyday.core.HolidayManager;
import de.focus_shift.jollyday.core.ManagerParameters;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.scheduledday.Scheduledday;
import nl.itvitae.rooster.scheduledday.ScheduleddayRepository;
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
  private final FreeDayService freeDayService;
  private final ScheduleddayRepository scheduleddayRepository;

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
    FreeDay savedFreeday = freeDayService.addFreeDay(freeDay);
    URI locationOfFreeDay = ucb.path("/api/v1/freedays").buildAndExpand(freeDay.getId()).toUri();
    return ResponseEntity.created(locationOfFreeDay).body(savedFreeday);
  }


  @PostMapping("/multi")
  public ResponseEntity<?> addFreeDays(@RequestBody MultiFreeDaysRequest request, UriComponentsBuilder ucb) {
    final HolidayManager holidayManager = HolidayManager.getInstance(
        ManagerParameters.create(NETHERLANDS));
    final List<Holiday> holidays = new ArrayList<>(holidayManager.getHolidays(request.year()));
    holidays.sort(Comparator.comparing(Holiday::getDate));

    for (int i = 0; i < holidays.size(); i++) {
      Holiday holiday = holidays.get(i);
      if (freeDayRepository.existsByDate(holiday.getDate()) || !request.array()[i]) continue;
      freeDayService.addFreeDay(new FreeDay(holiday.getDate(),
          holiday.getDescription()));
    }
    URI locationOfFreeDays = ucb.path("/api/v1/freedays").build().toUri();
    return ResponseEntity.created(locationOfFreeDays).body(holidays);
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
