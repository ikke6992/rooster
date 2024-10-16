package nl.itvitae.rooster.field;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldService {

  private final FieldRepository fieldRepository;

  public List<Field> getAll() {
    return fieldRepository.findAll();
  }

  public Field getById(long id) {
    return fieldRepository.findById(id).get();
  }

  public Field addField(String name, int daysPhase1, int daysPhase2, int daysPhase3) {
    return fieldRepository.save(new Field(name, daysPhase1, daysPhase2, daysPhase3));
  }
}
