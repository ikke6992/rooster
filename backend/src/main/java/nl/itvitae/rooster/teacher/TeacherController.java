package nl.itvitae.rooster.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/teachers")
public class TeacherController {

  private final TeacherService teacherService;

  public List<Teacher> getAll() {
    return teacherService.getAll();
  }
}
