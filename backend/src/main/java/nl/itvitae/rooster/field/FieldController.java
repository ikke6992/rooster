package nl.itvitae.rooster.field;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin
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
    }
    final Field field = fieldService.addField(request.name(), request.daysPhase1(), request.daysPhase2(), request.daysPhase3());
    URI locationOfField = ucb.path("/api/v1/fields").buildAndExpand(field.getId()).toUri();
    return ResponseEntity.created(locationOfField).body(field);
  }
}
