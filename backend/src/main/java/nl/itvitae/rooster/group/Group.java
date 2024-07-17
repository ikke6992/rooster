package nl.itvitae.rooster.group;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "groups")
public class Group {

  @Id
  @GeneratedValue
  private long id;

  private int groupNumber;
  private String color;
  private int numberOfStudents;

  public Group(int groupNumber, String color, int numberOfStudents) {
    this.groupNumber = groupNumber;
    this.color = color;
    this.numberOfStudents = numberOfStudents;
  }
}
