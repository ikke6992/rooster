package nl.itvitae.rooster.teacher;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.group.Group;

@Getter
@Setter
@NoArgsConstructor
@Entity(name= "group_teacher")
public class GroupTeacher {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "group_id")
  @JsonBackReference
  private Group group;

  @ManyToOne
  @JoinColumn(name = "teacher_id")
  @JsonBackReference
  private Teacher teacher;

  private int daysPhase1;
  private int daysPhase2;
  private int daysPhase3;

  public GroupTeacher(Group group, Teacher teacher, int daysPhase1, int daysPhase2, int daysPhase3) {
    this.group = group;
    this.teacher = teacher;
    this.daysPhase1 = daysPhase1;
    this.daysPhase2 = daysPhase2;
    this.daysPhase3 = daysPhase3;
  }
}
