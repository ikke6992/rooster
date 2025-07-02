package nl.itvitae.rooster.scheduledday;

import java.io.IOException;
import java.net.URI;
import lombok.AllArgsConstructor;
import nl.itvitae.rooster.group.Group;
import nl.itvitae.rooster.group.GroupRepository;
import nl.itvitae.rooster.lesson.Lesson;
import nl.itvitae.rooster.lesson.LessonService;
import nl.itvitae.rooster.teacher.Teacher;
import nl.itvitae.rooster.teacher.TeacherRepository;
import nl.itvitae.rooster.freeday.FreeDay;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomService;
import nl.itvitae.rooster.freeday.FreeDayRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin("http://localhost:4200")
@AllArgsConstructor
@RequestMapping("api/v1/scheduleddays")
public class ScheduleddayController {

  private final ScheduleddayRepository scheduleddayRepository;
  private final ScheduleddayService scheduleddayService;
  private final ClassroomService classroomService;
  private final FreeDayRepository freeDayRepository;
  private final GroupRepository groupRepository;
  private final LessonService lessonService;
  private final TeacherRepository teacherRepository;

  @GetMapping
  public ResponseEntity<?> getAll() {
    return ResponseEntity.ok(
        scheduleddayService.findAll().stream().map(ScheduleddayDTO::new).toList());
  }

  @PostMapping  public ResponseEntity<?> addScheduledDay(@RequestBody ScheduledDayRequest scheduledDayRequest, UriComponentsBuilder ucb){
    Optional<Classroom> classroom = classroomService.getById(1);
    Optional<Group> group = groupRepository.findByGroupNumber(scheduledDayRequest.groupNumber());
    Optional<Teacher> teacher = teacherRepository.findById(scheduledDayRequest.teacherId());
    LocalDate date = LocalDate.parse(scheduledDayRequest.date());
    Lesson lesson;

    if (group.isEmpty()){
      return ResponseEntity.badRequest().body("Group does not exist");
    }
    if (freeDayRepository.existsByDate(date) || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY){
      return ResponseEntity.badRequest().body("Selected day is on a weekend/freeday");
    }
    if (teacher.isEmpty()){
      lesson = lessonService.createLesson(group.get());
    } else if (scheduleddayRepository.existsByDateAndLessonTeacher(date, teacher.get())){
      return ResponseEntity.badRequest().body("Teacher is already scheduled for that day");}
    else {
      lesson = lessonService.createLesson(group.get(), teacher.get());
    }
    if (scheduleddayRepository.existsByDateAndLessonGroup(date, lesson
        .getGroup())){
      return ResponseEntity.badRequest().body("Group is already scheduled on this day");
    }
    Scheduledday scheduledday = scheduleddayService.addScheduledday(0, date, classroom.get(), lesson);
    URI locationOfScheduledDay = ucb.path("/api/v1/scheduledday").buildAndExpand(scheduledday.getId()).toUri();
    return ResponseEntity.created(locationOfScheduledDay).body(scheduledday);
  }

  @GetMapping("/month/{month}/{year}")
  public ResponseEntity<?> getAllByMonth(@PathVariable int month, @PathVariable int year) {
    return ResponseEntity.ok(
        scheduleddayService.findAllByMonth(month, year).stream().map(ScheduleddayDTO::new)
            .toList());
  }

  @GetMapping("/export")
  public ResponseEntity<?> exportExcel() throws IOException {
    var body = scheduleddayService.createExcel();
    if (body == null) {
      return ResponseEntity.badRequest().body("No days planned for this year");
    }
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "attachment; filename=scheduleddays.xlsx");

    return ResponseEntity.ok().headers(headers)
        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
        .body(new InputStreamResource(body));
  }

  @PutMapping("/override/{id}")
  public ResponseEntity<?> overrideScheduling(@PathVariable long id, @RequestBody OverrideRequest overrideRequest) {
    Scheduledday scheduledday = scheduleddayService.findById(id);
    LocalDate date = LocalDate.parse(overrideRequest.date());
    Optional<Classroom> classroom = classroomService.getById(overrideRequest.classroomId());
    if (classroom.isEmpty()) {
      return ResponseEntity.badRequest().body("Classroom does not exist");
    }
    if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)
        || freeDayRepository.existsByDate(date)){
      return ResponseEntity.badRequest().body("Day cannot be scheduled on weekends on freedays");
    }
    if (scheduleddayRepository.existsByDateAndClassroom(date, classroom.get())
        || (!date.equals(scheduledday.getDate()) && scheduleddayRepository.existsByDateAndLessonGroup(
            date, scheduledday.getLesson().getGroup()))) {

      return ResponseEntity.badRequest().body("Day + Classroom are already scheduled");
    }
    return ResponseEntity.ok(scheduleddayService.overrideScheduling(
        scheduledday, date, classroom.get(), overrideRequest.adaptWeekly()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> removeScheduledday(@PathVariable Long id){
    Optional<Scheduledday> optScheduledday = scheduleddayRepository.findById(id);
    if (optScheduledday.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    scheduleddayRepository.delete(optScheduledday.get());
    return ResponseEntity.noContent().build();
  }
}
