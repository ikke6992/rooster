package nl.itvitae.rooster.group;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

  private final GroupRepository groupRepository;

  public List<Group> getAll() {
    return groupRepository.findAll();
  }

  public Group addGroup(Group group) {
    groupRepository.save(group);
    return group;
  }
}
