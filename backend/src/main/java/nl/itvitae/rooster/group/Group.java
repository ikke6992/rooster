package nl.itvitae.rooster.group;

import jakarta.persistence.*;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.lesson.Lesson;
import nl.itvitae.rooster.teacher.Teacher;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "groups")
public class Group {

  @Id
  @GeneratedValue
  private Long id;

  private int groupNumber;
  private String color;
  private int numberOfStudents;
  @OneToMany
  private List<Lesson> lessons;
  @ManyToMany(mappedBy = "groups")
  private List<Teacher> teachers;

  public Group(int groupNumber, String color, int numberOfStudents) {
    this.groupNumber = groupNumber;
    this.color = color;
    this.numberOfStudents = numberOfStudents;
  }
}
