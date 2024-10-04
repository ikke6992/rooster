package nl.itvitae.rooster.teacher;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.MyDay;
import nl.itvitae.rooster.group.Group;
import nl.itvitae.rooster.lesson.Lesson;

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

  @ManyToMany
  @JoinTable(
      name = "teacher_day",
      joinColumns = @JoinColumn(name = "teacher_id"),
      inverseJoinColumns = @JoinColumn(name = "day_id"))
  private List<MyDay> availability;

  private int maxDaysPerWeek;

  @OneToMany
  private List<Lesson> lessons = new ArrayList<>();

  public Teacher(String name, List<MyDay> availability, int maxDaysPerWeek) {
    this.name = name;
    this.availability = availability;
    this.maxDaysPerWeek = maxDaysPerWeek;
  }

  public void addGroup(Group group) {
    groups.add(group);
  }

  public void addLesson(Lesson lesson) {
    lessons.add(lesson);
  }
}
