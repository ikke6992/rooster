package nl.itvitae.rooster.group;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.group.vacation.Vacation;
import nl.itvitae.rooster.lesson.Lesson;
import nl.itvitae.rooster.teacher.GroupTeacher;
import org.hibernate.annotations.*;

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
  private List<Lesson> lessons = new ArrayList<>();
  @OneToMany
  private List<Vacation> vacations = new ArrayList<>();
  @OneToMany(mappedBy = "group")
  private List<GroupTeacher> groupTeachers = new ArrayList<>();

  private String field;

  private LocalDate startDate;
  private int daysPhase1;
  private int weeksPhase1;
  private int daysPhase2;
  private int weeksPhase2;
  private int daysPhase3;
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
      String field,
      LocalDate startDate,
      int daysPhase1,
      int weeksPhase1,
      int daysPhase2,
      int weeksPhase2,
      int daysPhase3,
      int weeksPhase3
  ) {
    this.groupNumber = groupNumber;
    this.color = color;
    this.numberOfStudents = numberOfStudents;
    this.field = field;
    this.startDate = startDate;
    this.daysPhase1 = daysPhase1;
    this.weeksPhase1 = weeksPhase1;
    this.daysPhase2 = daysPhase2;
    this.weeksPhase2 = weeksPhase2;
    this.daysPhase3 = daysPhase3;
    this.weeksPhase3 = weeksPhase3;
  }

  public void addGroupTeacher(GroupTeacher groupTeacher) {
    groupTeachers.add(groupTeacher);
  }
  public void addVacation(Vacation vacation) {
    vacations.add(vacation);
  }

  public void removeVacation(Vacation vacation) {
    vacations.remove(vacation);
  }

  public void addLesson(Lesson lesson) {
    lessons.add(lesson);
  }
  public void removeLesson(Lesson lesson) {
    lessons.remove(lesson);
  }
}
