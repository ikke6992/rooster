package nl.itvitae.rooster;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.classroom.ClassroomRepository;
import nl.itvitae.rooster.field.Field;
import nl.itvitae.rooster.field.FieldRepository;
import nl.itvitae.rooster.group.Group;
import nl.itvitae.rooster.group.GroupService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

  private final ClassroomRepository classroomRepository;
  private final GroupService groupService;
  private final FieldRepository fieldRepository;

  @Override
  public void run(String... args) throws Exception {
    var classroom1 = saveClassroom(12, true, false);
    var classroom2 = saveClassroom(20, true, false);
    var classroom3 = saveClassroom(20, true, false);
    var classroom4 = saveClassroom(12, false, true);
    var classroom5 = saveClassroom(25, true, false);
    var classroom6 = saveClassroom(14, true, true);

    var java = saveField("Java", 3, 4, 2);
    var data = saveField("Data", 3, 4, 3);
    var cloud = saveField("Cloud", 2, 4, 2);
    var security = saveField("Security", 3, 3, 2);

    var group53 = saveGroup(53, "#ffa500", 12, java);

  }

  private Classroom saveClassroom(int capacity, boolean hasBeamer, boolean forPracticum) {
    return classroomRepository.save(new Classroom(capacity, hasBeamer, forPracticum));
  }

  private Group saveGroup(int groupNumber, String colour,int numberOfStudents, Field field) {
    Group group = groupService.addGroup(groupNumber, colour, numberOfStudents, field, LocalDate.now(), 8, 12, 8);
    groupService.scheduleGroup(group);
    return group;
  }

  private Field saveField(String name, int daysPhase1, int daysPhase2, int daysPhase3) {
    return fieldRepository.save(new Field(name, daysPhase1, daysPhase2, daysPhase3));
  }

}
