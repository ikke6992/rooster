package nl.itvitae.rooster.scheduledday;

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
@Entity
public class Scheduledday {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate date;

//  @ManyToOne
//  private Classroom classroom;
//
//  @OneToOne
//  private Lesson lesson;
}
