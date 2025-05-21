package nl.itvitae.rooster.group;

import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.group.vacation.ArchivedVacationRepository;
import nl.itvitae.rooster.group.vacation.VacationRequest;
import nl.itvitae.rooster.teacher.Teacher;
import nl.itvitae.rooster.teacher.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class GroupController {

  private final GroupService groupService;
  private final ArchivedGroupRepository archivedGroupRepository;
  private final TeacherService teacherService;

  private final GroupRepository groupRepository;

  @GetMapping
  public ResponseEntity<List<GroupDTO>> getAll() {
    return ResponseEntity.ok(groupService.getAll().stream().map(GroupDTO::of).toList());
  }

  @GetMapping("/archived")
  public ResponseEntity<List<GroupDTO>> getArchived() {
    return ResponseEntity.ok(groupService.getArchived().stream().map(GroupDTO::ofArchived).toList());
  }

  @PostMapping("/new")
  public ResponseEntity<?> addGroup(@RequestBody GroupRequest request, UriComponentsBuilder ucb) {
    if (request.weeksPhase1() < 1 || request.weeksPhase2() < 1 || request.weeksPhase3() < 1) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount of weeks needs to be greater than 0");
    }
    for (AssignmentRequest teacherAssignment : request.teacherAssignments()) {
      if (teacherAssignment.daysPhase1() < 0 || teacherAssignment.daysPhase1() > 5
          || teacherAssignment.daysPhase2() < 0 || teacherAssignment.daysPhase2() > 5
          || teacherAssignment.daysPhase3() < 0 || teacherAssignment.daysPhase3() > 5) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount of days needs to be between 0 and 5");
      }
      if (teacherAssignment.daysPhase1() == 0 && teacherAssignment.daysPhase2() == 0
          && teacherAssignment.daysPhase3() == 0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Needs to be assigned at least 1 day in at least 1 phase");
      }
    }
    if (groupRepository.findByGroupNumber(request.groupNumber()).isPresent()
        || archivedGroupRepository.findByGroupNumber(request.groupNumber()).isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Group with number " + request.groupNumber() + " already exists.");
    }
    if (groupService.checkSimilarColor(request.color())) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Color is too similar to color of other group.");
    }
    final LocalDate startDate = LocalDate.parse(request.startDate());
    final Group group = groupService.addGroup(request.groupNumber(), request.color(), request.numberOfStudents(),
        request.field(), startDate, request.daysPhase1(), request.weeksPhase1(),
        request.daysPhase2(), request.weeksPhase2(), request.daysPhase3(), request.weeksPhase3());
    for (AssignmentRequest teacherAssignment : request.teacherAssignments()) {
      teacherService.addGroup(teacherService.getById(teacherAssignment.id()), group,
          teacherAssignment.daysPhase1(), teacherAssignment.daysPhase2(), teacherAssignment.daysPhase3());
    }
    groupService.scheduleGroup(group);
    URI locationOfGroup = ucb.path("/api/v1/groups").buildAndExpand(group.getId()).toUri();
    return ResponseEntity.created(locationOfGroup).body(group);
  }

  @PutMapping("/{number}/edit")
  public ResponseEntity<?> editGroup(@PathVariable int number, @RequestBody GroupRequest request) {
    if (request.weeksPhase1() < 1 || request.weeksPhase2() < 1 || request.weeksPhase3() < 1) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount of weeks needs to be greater than 0");
    }

    Optional<Group> existingGroup = groupRepository.findByGroupNumber(number);
    if (existingGroup.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group " + number + " does not exist");
    } else {
      final Group group = groupService.editGroup(existingGroup.get(), request.groupNumber(), request.color(),
          request.numberOfStudents(), request.field(), LocalDate.parse(request.startDate()),
          request.daysPhase1(), request.weeksPhase1(), request.daysPhase2(), request.weeksPhase2(),
          request.daysPhase3(), request.weeksPhase3());
      groupService.rescheduleGroup(
          group, group.getStartDate().isAfter(LocalDate.now()) ? group.getStartDate() : LocalDate.now());
      return ResponseEntity.ok(GroupDTO.of(group));
    }
  }

  @PutMapping("/{number}/reschedule")
  public ResponseEntity<?> rescheduleGroup(@PathVariable int number) {
    Optional<Group> group = groupRepository.findByGroupNumber(number);
    if (group.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    if (number == 0) {
      return ResponseEntity.ok(GroupDTO.of(groupService.rescheduleReturnDay(group.get())));
    }
    return ResponseEntity.ok(
        GroupDTO.of(groupService.rescheduleGroup(group.get(), LocalDate.now())));
  }

  @PutMapping("/{number}/addVacation")
  public ResponseEntity<?> addVacation(@PathVariable int number, @RequestBody VacationRequest request) {
    if (request.weeks() < 1) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount of weeks needs to be greater than 0");
    }
    Group group = groupRepository.findByGroupNumber(number).get();
    return ResponseEntity.ok(GroupDTO.of(groupService.addVacation(
        group, LocalDate.parse(request.startDate()), request.weeks())));
  }

  @DeleteMapping("/{number}/archive")
  public ResponseEntity<?> deleteGroup(@PathVariable int number) {
    Optional<Group> group = groupRepository.findByGroupNumber(number);
    if (group.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(GroupDTO.ofArchived(groupService.deleteGroup(group.get())));
  }
}
