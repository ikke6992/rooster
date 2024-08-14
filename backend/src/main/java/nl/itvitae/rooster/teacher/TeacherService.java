package nl.itvitae.rooster.teacher;

import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.MyDay;
import nl.itvitae.rooster.MyDayRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

  private final TeacherRepository teacherRepository;
  private final MyDayRepository myDayRepository;

  public List<Teacher> getAll() {
    return teacherRepository.findAll();
  }

  public Teacher getById(long id) {
    return teacherRepository.findById(id).get();
  }

  public Teacher setAvailability(long id, boolean[] availability, int maxDays) {
    Teacher teacher = getById(id);
    List<MyDay> setAvailability = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      if (availability[i]) {
        setAvailability.add(myDayRepository.findById((long)(i+1)).get());
      }
    }
    teacher.setAvailability(setAvailability);
    teacher.setMaxDaysPerWeek(maxDays);
    teacherRepository.save(teacher);
    return teacher;
  }
}
