package nl.itvitae.rooster.teacher;

import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.MyDay;
import nl.itvitae.rooster.MyDayRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
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

  public Teacher setAvailability(long id, String[] availability, int maxDaysPerWeek) {
    Teacher teacher = getById(id);
    List<MyDay> newAvailability = new ArrayList<>();
    for (String day : availability) {
      newAvailability.add(myDayRepository.findByDay(DayOfWeek.valueOf(day)));
    }
    teacher.setAvailability(newAvailability);
    teacher.setMaxDaysPerWeek(maxDaysPerWeek);
    teacherRepository.save(teacher);
    return teacher;
  }
}
