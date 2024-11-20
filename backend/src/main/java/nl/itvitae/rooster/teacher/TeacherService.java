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
  private final GroupService groupService;
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

  public Teacher addGroup(long id, int groupNumber, int daysPhase1, int daysPhase2, int daysPhase3) {
    Teacher teacher = getById(id);
    Group group = groupRepository.findByGroupNumber(groupNumber).get();
    GroupTeacher groupTeacher = new GroupTeacher(group, teacher, daysPhase1, daysPhase2, daysPhase3);
    teacher.addGroupTeacher(groupTeacher);
    group.addGroupTeacher(groupTeacher);
    groupTeacherRepository.save(groupTeacher);
    groupRepository.save(group);
    groupService.rescheduleGroup(group, group.getStartDate().isAfter(LocalDate.now()) ? group.getStartDate() : LocalDate.now());
    teacherRepository.save(teacher);
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
