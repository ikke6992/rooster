package nl.itvitae.rooster.scheduledday;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.lesson.ArchivedLesson;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "archived_scheduleddays")
public class ArchivedScheduledday {

  @Id
  private Long id;

  private LocalDate date;

  @ManyToOne
  private Classroom classroom;

  @OneToOne
  private ArchivedLesson lesson;

  public ArchivedScheduledday(Scheduledday scheduledday, ArchivedLesson lesson) {
    this.id = scheduledday.getId();
    this.date = scheduledday.getDate();
    this.classroom = scheduledday.getClassroom();
    this.lesson = lesson;
  }
}
