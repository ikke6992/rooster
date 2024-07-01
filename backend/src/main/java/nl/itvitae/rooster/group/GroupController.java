package nl.itvitae.rooster.group;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
  public void addGroup(@RequestBody Group group) {
    groupService.addGroup(group);
  }
}
