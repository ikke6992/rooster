package nl.itvitae.rooster.teacher;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.MyDay;
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
  private boolean teachesPracticum;

  @ManyToMany
  @JoinTable(
      name = "group_teacher",
      joinColumns = @JoinColumn(name = "teacher_id"),
      inverseJoinColumns = @JoinColumn(name = "group_id"))
  private List<Group> groups = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "teacher_day",
      joinColumns = @JoinColumn(name = "teacher_id"),
      inverseJoinColumns = @JoinColumn(name = "day_id"))
  private List<MyDay> availability;

  private int maxDaysPerWeek;

  public Teacher(String name, boolean teachesPracticum, List<MyDay> availability, int maxDaysPerWeek) {
    this.name = name;
    this.teachesPracticum = teachesPracticum;
    this.availability = availability;
    this.maxDaysPerWeek = maxDaysPerWeek;
  }

  public void addGroup(Group group) {
    groups.add(group);
  }
}
