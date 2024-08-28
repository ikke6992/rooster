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

  public Teacher addTeacher(String name, boolean teachesPracticum, String[] availability, int maxDaysPerWeek) {
    List<MyDay> newAvailability = getAvailability(availability);
    int realMaxDaysPerWeek = Math.min(newAvailability.size(), maxDaysPerWeek);
    return teacherRepository.save(new Teacher(name, teachesPracticum, newAvailability, realMaxDaysPerWeek));
  }

  public Teacher setAvailability(long id, String[] availability, int maxDaysPerWeek) {
    Teacher teacher = getById(id);
    List<MyDay> newAvailability = getAvailability(availability);
    teacher.setAvailability(newAvailability);
    teacher.setMaxDaysPerWeek(Math.min(newAvailability.size(), maxDaysPerWeek));
    teacherRepository.save(teacher);
    return teacher;
  }

  public List<MyDay> getAvailability(String[] days) {
    List<MyDay> availability = new ArrayList<>();
    for (String day : days) {
      availability.add(myDayRepository.findByDay(DayOfWeek.valueOf(day)));
    }
    return availability;
  }
}