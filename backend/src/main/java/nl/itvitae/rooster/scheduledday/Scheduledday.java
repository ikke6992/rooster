package nl.itvitae.rooster.scheduledday;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.lesson.Lesson;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "scheduleddays")
public class Scheduledday {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate date;

  @ManyToOne
  private Classroom classroom;

  @OneToOne(orphanRemoval=true)
  private Lesson lesson;

  public Scheduledday(LocalDate date, Classroom classroom, Lesson lesson) {
    this.date = date;
    this.classroom = classroom;
    this.lesson = lesson;
  }
}
