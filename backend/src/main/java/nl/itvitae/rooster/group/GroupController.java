package nl.itvitae.rooster.group;

import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.field.Field;
import nl.itvitae.rooster.field.FieldService;
import nl.itvitae.rooster.group.vacation.VacationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class GroupController {

  private final GroupService groupService;
  private final GroupRepository groupRepository;
  private final FieldService fieldService;

  @GetMapping
  public ResponseEntity<List<GroupDTO>> getAll() {
    return ResponseEntity.ok(groupService.getAll().stream().map(GroupDTO::of).toList());
  }

  @PostMapping("/new")
  public ResponseEntity<?> addGroup(@RequestBody GroupRequest request, UriComponentsBuilder ucb) {

    if (groupRepository.findByGroupNumber(request.groupNumber()).isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Group with number " + request.groupNumber() + " already exists.");
    }
    if (groupService.checkSimilarColor(request.color())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Color is too similar to color of other group.");
    }
    if (request.weeksPhase1() < 1 || request.weeksPhase2() < 1 || request.weeksPhase3() < 1) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount of weeks needs to be greater than 0");
    }
    final Field field = fieldService.getById(request.field());
    final LocalDate startDate = LocalDate.parse(request.startDate());
    final Group group = groupService.addGroup(request.groupNumber(), request.color(),
        request.numberOfStudents(), field, startDate, request.weeksPhase1(), request.weeksPhase2(),
        request.weeksPhase3());
    groupService.scheduleGroup(group);
    URI locationOfGroup = ucb.path("/api/v1/groups").buildAndExpand(group.getId()).toUri();
    return ResponseEntity.created(locationOfGroup).body(group);
  }

  @PutMapping("/{number}/edit")
  public ResponseEntity<?> editGroup(@PathVariable int number, @RequestBody GroupRequest request) {
    Optional<Group> existingGroup = groupRepository.findByGroupNumber(number);
    if (existingGroup.isEmpty()) {
      return ResponseEntity.badRequest().build();
    } else {
      final Group group = groupService.editGroup(existingGroup.get(), request.groupNumber(), request.color(),
          request.numberOfStudents(), fieldService.getById(request.field()), LocalDate.parse(request.startDate()),
          request.weeksPhase1(), request.weeksPhase2(), request.weeksPhase3());
      groupService.rescheduleGroup(group, LocalDate.now());
      return ResponseEntity.ok(GroupDTO.of(group));
    }
  }

  @PutMapping("/{number}/reschedule")
  public ResponseEntity<?> rescheduleGroup(@PathVariable int number) {
    Optional<Group> group = groupRepository.findByGroupNumber(number);
    if (group.isEmpty()) {
      return ResponseEntity.badRequest().build();
    } else if (number == 0) {
      return ResponseEntity.ok(GroupDTO.of(groupService.rescheduleReturnDay(group.get())));
    } else {
      return ResponseEntity.ok(GroupDTO.of(groupService.rescheduleGroup(group.get(), LocalDate.now())));
    }
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
}
