package nl.itvitae.rooster;

import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.group.Group;
import nl.itvitae.rooster.group.GroupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestSeeder implements CommandLineRunner {

  private final GroupRepository groupRepository;

  @Override
  public void run(String... args) throws Exception {
    if (groupRepository.count() == 0) {
      groupRepository.save(new Group(53, "orange", 10));
    }

  }
}
