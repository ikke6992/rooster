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
}
