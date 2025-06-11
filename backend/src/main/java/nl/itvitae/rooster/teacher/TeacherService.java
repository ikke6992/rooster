package nl.itvitae.rooster.teacher;

import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.MyDay;
import nl.itvitae.rooster.MyDayRepository;
import nl.itvitae.rooster.group.Group;
import nl.itvitae.rooster.group.GroupRepository;
import nl.itvitae.rooster.group.GroupService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

  private final TeacherRepository teacherRepository;
  private final GroupRepository groupRepository;
  private final MyDayRepository myDayRepository;
  private final GroupTeacherRepository groupTeacherRepository;

  public List<Teacher> getAll() {
    return teacherRepository.findAll();
  }

  public Teacher getById(long id) {
    return teacherRepository.findById(id).get();
  }

  public Teacher addTeacher(String name, String[] availability, int maxDaysPerWeek) {
    List<MyDay> newAvailability = getAvailability(availability);
    int realMaxDaysPerWeek = Math.min(newAvailability.size(), maxDaysPerWeek);
    return teacherRepository.save(new Teacher(name, newAvailability, realMaxDaysPerWeek));
  }

  public Teacher addGroup(Teacher teacher, Group group, int daysPhase1, int daysPhase2, int daysPhase3) {
    GroupTeacher groupTeacher = new GroupTeacher(group, teacher, daysPhase1, daysPhase2, daysPhase3);
    teacher.addGroupTeacher(groupTeacher);
    group.addGroupTeacher(groupTeacher);
    groupTeacherRepository.save(groupTeacher);
    groupRepository.save(group);
    teacherRepository.save(teacher);
    return teacher;
  }

  public Teacher removeGroup(GroupTeacher groupTeacher) {
    Group group = groupTeacher.getGroup();
    Teacher teacher = groupTeacher.getTeacher();
    group.removeGroupTeacher(groupTeacher);
    teacher.removeGroupTeacher(groupTeacher);
    groupRepository.save(group);
    teacherRepository.save(teacher);
    groupTeacherRepository.delete(groupTeacher);
    return teacher;
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
