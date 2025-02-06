package nl.itvitae.rooster.field;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fields")
public class FieldController {

  private final FieldService fieldService;
  private final FieldRepository fieldRepository;

  @GetMapping
  public List<Field> getAll() {
    return fieldService.getAll();
  }

  @GetMapping("/{id}")
  public Field getById(@RequestParam long id) {
    return fieldService.getById(id);
  }

  @PostMapping("/new")
  public ResponseEntity<?> addField(@RequestBody FieldRequest request, UriComponentsBuilder ucb) {
    if (fieldRepository.findByName(request.name()).isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Field " + request.name() + " already exists.");
    } else if (request.daysPhase1() < 1 || request.daysPhase1() > 5 || request.daysPhase2() < 1
        || request.daysPhase2() > 5 || request.daysPhase3() < 1 || request.daysPhase3() > 5) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount of days needs to be between 1 and 5");
    } else {
      final Field field = fieldService.addField(
          request.name(), request.daysPhase1(), request.daysPhase2(), request.daysPhase3());
      URI locationOfField = ucb.path("/api/v1/fields").buildAndExpand(field.getId()).toUri();
      return ResponseEntity.created(locationOfField).body(field);
    }
  }

  @PutMapping("/{id}/edit")
  public ResponseEntity<?> editField(@PathVariable long id, @RequestBody FieldRequest request) {
    Optional<Field> field = fieldRepository.findById(id);
    if (field.isEmpty()) {
      return ResponseEntity.badRequest().build();
    } else if (request.daysPhase1() < 1 || request.daysPhase1() > 5 || request.daysPhase2() < 1
        || request.daysPhase2() > 5 || request.daysPhase3() < 1 || request.daysPhase3() > 5) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount of days needs to be between 1 and 5");
    } else {
      return ResponseEntity.ok(fieldService.editField(
          field.get(), request.name(), request.daysPhase1(), request.daysPhase2(), request.daysPhase3()));
    }
  }
}
