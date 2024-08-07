package nl.itvitae.rooster.group;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.lesson.Lesson;
import nl.itvitae.rooster.field.Field;


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
  @OneToMany
  private List<Lesson> lessons;

  @ManyToOne
  private Field field;

  private LocalDate startDate;
  private int weeksPhase1;
  private int weeksPhase2;
  private int weeksPhase3;

  public Group(int groupNumber, String color, int numberOfStudents) {
    this.groupNumber = groupNumber;
    this.color = color;
    this.numberOfStudents = numberOfStudents;
  }

  public Group(
      int groupNumber,
      String color,
      int numberOfStudents,
      Field field,
      LocalDate startDate,
      int weeksPhase1,
      int weeksPhase2,
      int weeksPhase3
  ) {
    this.groupNumber = groupNumber;
    this.color = color;
    this.numberOfStudents = numberOfStudents;
    this.field = field;
    this.startDate = startDate;
    this.weeksPhase1 = weeksPhase1;
    this.weeksPhase2 = weeksPhase2;
    this.weeksPhase3 = weeksPhase3;
  }
}
