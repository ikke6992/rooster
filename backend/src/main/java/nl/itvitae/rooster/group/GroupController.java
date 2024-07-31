package nl.itvitae.rooster.group;

import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.field.Field;
import nl.itvitae.rooster.field.FieldService;
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
  private final FieldService fieldService;

  @GetMapping("/")
  public List<Group> getAll() {
    return groupService.getAll();
  }

  @PostMapping("/new")
  public ResponseEntity<Group> addGroup(@RequestBody GroupRequest request, UriComponentsBuilder ucb) {
    List<Group> groups = getAll();
    for (Group exists : groups) {
      if (request.groupNumber() == exists.getGroupNumber()) {
        return ResponseEntity.badRequest().build();
      }
    }
    final Field field = fieldService.getById(request.field());
    final LocalDate startDate = LocalDate.parse(request.startDate());
    final Group group = groupService.addGroup(request.groupNumber(), request.color(), request.numberOfStudents(), field, startDate, request.weeksPhase1(), request.weeksPhase2(), request.weeksPhase3());
    URI locationOfGroup = ucb.path("/api/v1/groups").buildAndExpand(groups.size()).toUri();
    return ResponseEntity.created(locationOfGroup).body(group);
  }
}
