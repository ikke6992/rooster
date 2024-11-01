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

  public Field editField(Field field, String name, int daysPhase1, int daysPhase2, int daysPhase3) {
    field.setName(name);
    field.setDaysPhase1(daysPhase1);
    field.setDaysPhase2(daysPhase2);
    field.setDaysPhase3(daysPhase3);
    return fieldRepository.save(field);
  }
}
