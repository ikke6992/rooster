package nl.itvitae.rooster.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

  private final TeacherRepository teacherRepository;

  public List<Teacher> getAll() {
    return teacherRepository.findAll();
  }
}
