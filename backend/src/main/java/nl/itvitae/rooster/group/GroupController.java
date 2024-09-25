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
  public ResponseEntity<?> addGroup(@RequestBody GroupRequest request,
      UriComponentsBuilder ucb) {

    if (groupRepository.findByGroupNumber(request.groupNumber()).isPresent()) {
      return ResponseEntity.badRequest().build();
    }
    if (groupService.checkSimilarColor(request.color())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Color is too similar to color of other group.");
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

  @PutMapping("/{number}/addVacation")
  public ResponseEntity<?> addVacation(@PathVariable int number, @RequestBody VacationRequest request) {
    Group group = groupRepository.findByGroupNumber(number).get();
    return ResponseEntity.ok(GroupDTO.of(groupService.addVacation(group, LocalDate.parse(request.startDate()), request.weeks())));
  }
}
