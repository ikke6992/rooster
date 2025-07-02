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

      var classroom1 = saveClassroom(10, true, false);
      var classroom2 = saveClassroom(15, true, false);
      var classroom3 = saveClassroom(15, true, false);
      var classroom4 = saveClassroom(15, false, true);
      var classroom5 = saveClassroom(15, true, false);
      var classroom6 = saveClassroom(10, true, true);

      LocalDate returnDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
      var returnDay = groupService.addGroup(0, "#d3d3d3", 0, "Terugkomdag",
          returnDate, 1, 52, 1, 52, 1, 52);
      var group60 = groupService.addGroup(60, "#00ffff", 10, "Security",
          LocalDate.now().minusYears(1), 3, 8, 4, 12,
          4, 8);
      var group61 = groupService.addGroup(61, "#000000", 14, "Data",
          LocalDate.parse("2025-02-28"), 3, 12, 3, 8,
          3, 16);
      var group62 = groupService.addGroup(62, "#ff0000", 12, "Java",
          LocalDate.parse("2025-04-28"), 3, 12, 4, 12,
          4, 12);
      var group63 = groupService.addGroup(63, "#00ff00", 13, "Cyber",
          LocalDate.parse("2025-05-26"), 3, 12, 4, 12,
          4, 7);

      var meow = saveTeacher("meow", new ArrayList<>(List.of(monday, tuesday, wednesday, thursday)), 4);

      var wubbo = saveTeacherWithGroups("Wubbo", new ArrayList<>(List.of(monday, tuesday, wednesday, friday)),
          3, 1, 2, 2, group61, group63);
      var coen = saveTeacherWithGroups("Coen", new ArrayList<>(List.of(monday, thursday)), 2, 2, 2, 1,
          group61, group62);

      groupService.scheduleReturnDay(returnDay, 4L, DayOfWeek.WEDNESDAY);
      groupService.scheduleGroup(group61);
      groupService.scheduleGroup(group62);
      groupService.scheduleGroup(group63);

      final HolidayManager holidayManager = HolidayManager.getInstance(
          ManagerParameters.create(NETHERLANDS));
      final Set<Holiday> holidays = holidayManager.getHolidays(LocalDate.now().minusYears(1),
          LocalDate.now().plusYears(2));
      for (Holiday holiday : holidays) {
        freeDayService.addFreeDay(new FreeDay(holiday.getDate(),
            holiday.getDescription()));
      }

      groupService.addVacation(group61, LocalDate.now().plusMonths(1), 2);

      int emptyLessons = 0;
      Long lessonId = scheduleddayRepository.findByDate(LocalDate.now()).getLast().getLesson().getId();
      for (int i = 1; i <= 10; i++) {
        Optional<Lesson> lesson = lessonRepository.findById(lessonId - emptyLessons - i);
        while (lesson.isEmpty()) {
          emptyLessons += 1;
          lesson = lessonRepository.findById(lessonId - emptyLessons - i);
        }
        addNote(lesson.get(), "Linux les " + (11 - i) + "/10", false);
      }
      addNote(lessonRepository.findById(lessonId).get(), "Linux Examen", true);

      groupService.deleteGroup(group60);
      userRepository.save(new User("admin", passwordEncoder.encode("admin"), Role.ROLE_ADMIN));
    }
  }

  private Teacher saveTeacher(String name, List<MyDay> availability, int maxDaysPerWeek) {
    Teacher teacher = new Teacher(name, availability, maxDaysPerWeek);
    teacherRepository.save(teacher);
    return teacher;
  }

  private Teacher saveTeacherWithGroups(String name, List<MyDay> availability, int maxDaysPerWeek,
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
