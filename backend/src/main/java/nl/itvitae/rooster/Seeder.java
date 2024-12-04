package nl.itvitae.rooster;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomRepository;
import nl.itvitae.rooster.field.Field;
import nl.itvitae.rooster.field.FieldRepository;
import nl.itvitae.rooster.freeday.FreeDay;
import nl.itvitae.rooster.freeday.FreeDayService;
import nl.itvitae.rooster.group.Group;
import nl.itvitae.rooster.group.GroupRepository;
import nl.itvitae.rooster.group.GroupService;
import nl.itvitae.rooster.scheduledday.ScheduleddayRepository;
import nl.itvitae.rooster.teacher.GroupTeacher;
import nl.itvitae.rooster.teacher.GroupTeacherRepository;
import nl.itvitae.rooster.lesson.Lesson;
import nl.itvitae.rooster.lesson.LessonRepository;
import nl.itvitae.rooster.teacher.Teacher;
import nl.itvitae.rooster.teacher.TeacherRepository;
import nl.itvitae.rooster.user.User;
import nl.itvitae.rooster.user.User.Role;
import nl.itvitae.rooster.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import de.focus_shift.jollyday.core.Holiday;
import de.focus_shift.jollyday.core.HolidayManager;
import de.focus_shift.jollyday.core.ManagerParameters;

import static de.focus_shift.jollyday.core.HolidayCalendar.NETHERLANDS;

@Component
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

  private final MyDayRepository myDayRepository;
  private final ClassroomRepository classroomRepository;
  private final FieldRepository fieldRepository;
  private final TeacherRepository teacherRepository;
  private final GroupRepository groupRepository;
  private final GroupTeacherRepository groupTeacherRepository;
  private final ScheduleddayRepository scheduleddayRepository;

  private final GroupService groupService;
  private final FreeDayService freeDayService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final LessonRepository lessonRepository;

  @Override
  public void run(String... args) throws Exception {
    if (scheduleddayRepository.count() == 0) {
      var monday = saveDay(DayOfWeek.MONDAY);
      var tuesday = saveDay(DayOfWeek.TUESDAY);
      var wednesday = saveDay(DayOfWeek.WEDNESDAY);
      var thursday = saveDay(DayOfWeek.THURSDAY);
      var friday = saveDay(DayOfWeek.FRIDAY);

      var classroom1 = saveClassroom(12, true, false);
      var classroom2 = saveClassroom(20, true, false);
      var classroom3 = saveClassroom(20, true, false);
      var classroom4 = saveClassroom(12, false, true);
      var classroom5 = saveClassroom(25, true, false);
      var classroom6 = saveClassroom(14, true, true);

      var java = saveField("Java", 3, 4, 2);
      var data = saveField("Data", 3, 4, 3);
      var cloud = saveField("Cloud", 2, 4, 2);
      var security = saveField("Security", 3, 3, 2);
      var returning = saveField("Returnday", 1, 1, 1);

      LocalDate returnDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
      var returnDay = groupService.addGroup(0, "#d3d3d3", 0, returning, returnDate, 52, 52, 52);

      groupService.scheduleReturnDay(returnDay, 4L, DayOfWeek.WEDNESDAY);

      final HolidayManager holidayManager = HolidayManager.getInstance(
          ManagerParameters.create(NETHERLANDS));
      final Set<Holiday> holidays = holidayManager.getHolidays(LocalDate.now(),
          LocalDate.now().plusYears(2));
      for (Holiday holiday : holidays) {
        freeDayService.addFreeDay(new FreeDay(holiday.getDate(),
            holiday.getDescription()));
      }

      userRepository.save(new User("admin", passwordEncoder.encode("admin"), Role.ROLE_ADMIN));
    }
  }

  private Group saveGroup(int groupNumber, String color, int numberOfStudents, Field field) {
    return groupService.addGroup(groupNumber, color, numberOfStudents, field,
        LocalDate.now().minusWeeks(4), 8, 12, 8);
  }

  private Teacher saveTeacher(String name, List<MyDay> availability, int maxDaysPerWeek,
      int daysPhase1, int daysPhase2, int daysPhase3, Group... groups) {
    Teacher teacher = new Teacher(name, availability, maxDaysPerWeek);
    teacherRepository.save(teacher);
    for (Group group : groups) {
      GroupTeacher groupTeacher = new GroupTeacher(group, teacher, daysPhase1, daysPhase2,
          daysPhase3);
      groupTeacherRepository.save(groupTeacher);
      group.addGroupTeacher(groupTeacher);
      groupRepository.save(group);
      teacher.addGroupTeacher(groupTeacher);
      teacherRepository.save(teacher);
    }
    return teacher;
  }

  private MyDay saveDay(DayOfWeek day) {
    return myDayRepository.save(new MyDay(day));
  }

  private Classroom saveClassroom(int capacity, boolean hasBeamer, boolean forPracticum) {
    return classroomRepository.save(new Classroom(capacity, hasBeamer, forPracticum));
  }

  private Field saveField(String name, int daysPhase1, int daysPhase2, int daysPhase3) {
    return fieldRepository.save(new Field(name, daysPhase1, daysPhase2, daysPhase3));
  }

  private void addNote(Lesson lesson, String note, boolean isExam){
    lesson.setNote(note);
    lesson.setExam(isExam);
    lessonRepository.save(lesson);
  }
}
