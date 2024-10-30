package nl.itvitae.rooster.lesson;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.group.ArchivedGroup;
import nl.itvitae.rooster.scheduledday.ArchivedScheduledday;
import nl.itvitae.rooster.teacher.Teacher;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "archived_lessons")
public class ArchivedLesson {

  @Id
  private Long id;

  @OneToOne
  @JsonBackReference
  private ArchivedScheduledday scheduledday;

  @ManyToOne
  private ArchivedGroup group;

  @ManyToOne
  private Teacher teacher;

  private String note;

  public ArchivedLesson(Lesson lesson, ArchivedGroup group) {
    this.id = lesson.getId();
    this.group = group;
    this.teacher = lesson.getTeacher();
    this.note = lesson.getNote();
  }
}
