package nl.itvitae.rooster.teacher;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.group.Group;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="teachers")
public class Teacher {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @ManyToMany
  @JoinTable(
      name = "group_teacher",
      joinColumns = @JoinColumn(name = "teacher_id"),
      inverseJoinColumns = @JoinColumn(name = "group_id"))
  private List<Group> groups = new ArrayList<>();

  private String availability;

  public Teacher(String name, String availability) {
    this.name = name;
    this.availability = availability;
  }

  public void addGroup(Group group) {
    groups.add(group);
  }
}
