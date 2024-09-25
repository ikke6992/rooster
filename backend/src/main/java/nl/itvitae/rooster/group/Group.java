package nl.itvitae.rooster.group;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.field.Field;
import nl.itvitae.rooster.group.vacation.Vacation;
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
  @OneToMany
  private List<Vacation> vacations = new ArrayList<>();
  @ManyToMany(mappedBy = "groups")
  private List<Teacher> teachers = new ArrayList<>();

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

  public void addVacation(Vacation vacation) {
    vacations.add(vacation);
  }
  public void addTeacher(Teacher teacher) {
    teachers.add(teacher);
  }
}
