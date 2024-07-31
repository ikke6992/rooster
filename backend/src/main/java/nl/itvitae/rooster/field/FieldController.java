package nl.itvitae.rooster.field;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/fields")
public class FieldController {

  private final FieldService fieldService;

  @GetMapping("/")
  public List<Field> getAll() {
    return fieldService.getAll();
  }

  @GetMapping("/{id}")
  public Field getById(@RequestParam long id) {
    return fieldService.getById(id);
  }
}
