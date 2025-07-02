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

      LocalDate returnDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
      var returnDay = groupService.addGroup(0, "#d3d3d3", 0, "Terugkomdag",
          returnDate, 1, 52, 1, 52, 1, 52);
      var group52 = groupService.addGroup(52, "#00ffff", 10, "Security",
          LocalDate.now().minusYears(1), 3, 8, 4, 12,
          4, 8);
      var group53 = groupService.addGroup(53, "#ffa500", 12, "Java",
          LocalDate.now().minusWeeks(4), 3, 10, 4, 12,
          2, 10);
      var group54 = groupService.addGroup(54, "#ff0000", 8, "Data",
          LocalDate.now().minusWeeks(4), 3, 10, 3, 14,
          3, 10);
      var group55 = groupService.addGroup(55, "#000000", 10, "Java",
          LocalDate.now().plusWeeks(4), 3, 8, 4, 12,
          4, 10);

      var wubbo = saveTeacher("Wubbo", new ArrayList<>(List.of(monday, tuesday, wednesday, friday)),
          3, 1, 2, 2, group53, group55);
      var coen = saveTeacher("Coen", new ArrayList<>(List.of(monday, thursday)), 2, 2, 2, 1,
          group53, group55);

      groupService.scheduleReturnDay(returnDay, 4L, DayOfWeek.WEDNESDAY);
      groupService.scheduleGroup(group53);
      groupService.scheduleGroup(group54);
      groupService.scheduleGroup(group55);

      final HolidayManager holidayManager = HolidayManager.getInstance(
          ManagerParameters.create(NETHERLANDS));
      final Set<Holiday> holidays = holidayManager.getHolidays(LocalDate.now().minusYears(1),
          LocalDate.now().plusYears(2));
      for (Holiday holiday : holidays) {
        freeDayService.addFreeDay(new FreeDay(holiday.getDate(),
            holiday.getDescription()));
      }

      groupService.addVacation(group53, LocalDate.now().plusMonths(1), 2);

      int emptyLessons = 0;
      for (int i = 1; i <= 10; i++) {
        Optional<Lesson> lesson = lessonRepository.findById(361L - emptyLessons - i);
        while (lesson.isEmpty()) {
          emptyLessons += 1;
          lesson = lessonRepository.findById(361L - emptyLessons - i);
        }
        addNote(lesson.get(), "Linux les " + (10 - i) + "/10", false);
      }
      addNote(lessonRepository.findById(361L).get(), "Linux Examen", true);

      groupService.deleteGroup(group52);
      userRepository.save(new User("admin", passwordEncoder.encode("admin"), Role.ROLE_ADMIN));
    }
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

  private void addNote(Lesson lesson, String note, boolean isExam){
    lesson.setNote(note);
    lesson.setExam(isExam);
    lessonRepository.save(lesson);
  }
}
