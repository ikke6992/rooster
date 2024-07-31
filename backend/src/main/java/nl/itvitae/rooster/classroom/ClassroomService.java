package nl.itvitae.rooster.classroom;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClassroomService {
  private final ClassroomRepository classroomRepository;

  public List<Classroom> findAll() {
    return classroomRepository.findAll();
  }


}
