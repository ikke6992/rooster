package nl.itvitae.rooster.teacher;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.group.ArchivedGroup;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "archived_group_teacher")
public class ArchivedGroupTeacher {

  @Id
  private Long id;

  @ManyToOne
  @JoinColumn(name = "group_id")
  private ArchivedGroup group;

  @ManyToOne
  @JoinColumn(name = "teacher_id")
  private Teacher teacher;

  private int daysPhase1;
  private int daysPhase2;
  private int daysPhase3;

  public ArchivedGroupTeacher(GroupTeacher groupTeacher, ArchivedGroup group) {
    this.group = group;
    this.teacher = groupTeacher.getTeacher();
    this.daysPhase1 = groupTeacher.getDaysPhase1();
    this.daysPhase2 = groupTeacher.getDaysPhase2();
    this.daysPhase3 = groupTeacher.getDaysPhase3();
  }
}
