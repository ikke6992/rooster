package nl.itvitae.rooster.group;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.field.Field;
import nl.itvitae.rooster.group.vacation.ArchivedVacation;
import nl.itvitae.rooster.lesson.ArchivedLesson;
import nl.itvitae.rooster.teacher.ArchivedGroupTeacher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "archived_groups")
public class ArchivedGroup {

  @Id
  private Long id;

  private int groupNumber;
  private String color;
  private int numberOfStudents;
  @OneToMany
  private List<ArchivedLesson> lessons = new ArrayList<>();
  @OneToMany
  private List<ArchivedVacation> vacations = new ArrayList<>();
  @OneToMany(mappedBy = "group")
  private List<ArchivedGroupTeacher> groupTeachers = new ArrayList<>();

  @ManyToOne
  private Field field;

  private LocalDate startDate;
  private int daysPhase1;
  private int weeksPhase1;
  private int daysPhase2;
  private int weeksPhase2;
  private int daysPhase3;
  private int weeksPhase3;

  public ArchivedGroup(Group group) {
    this.id = group.getId();
    this.groupNumber = group.getGroupNumber();
    this.color = group.getColor();
    this.numberOfStudents = group.getNumberOfStudents();
    this.field = group.getField();
    this.startDate = group.getStartDate();
    this.daysPhase1 = group.getDaysPhase1();
    this.weeksPhase1 = group.getWeeksPhase2();
    this.daysPhase2 = group.getDaysPhase2();
    this.weeksPhase2 = group.getWeeksPhase2();
    this.daysPhase3 = group.getDaysPhase3();
    this.weeksPhase3 = group.getWeeksPhase3();
  }

  public void addVacation(ArchivedVacation vacation) {
    vacations.add(vacation);
  }
}
