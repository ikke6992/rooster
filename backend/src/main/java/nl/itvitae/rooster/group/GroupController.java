package nl.itvitae.rooster.group;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class GroupController {

  private final GroupService groupService;

  @GetMapping("/")
  public List<Group> getAll() {
    return groupService.getAll();
  }

  @PostMapping("/new")
  public ResponseEntity<Group> addGroup(@RequestBody Group group, UriComponentsBuilder ucb) {
    List<Group> groups = getAll();
    for (Group exists : groups) {
      if (group.getGroupNumber() == exists.getGroupNumber()) {
        return ResponseEntity.badRequest().build();
      }
    }
    final Group newGroup = groupService.addGroup(group);
    URI locationOfNewGroup = ucb.path("/api/v1/groups").buildAndExpand(groups.size()).toUri();
    return ResponseEntity.created(locationOfNewGroup).body(newGroup);
  }
}
