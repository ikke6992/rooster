package nl.itvitae.rooster.lesson.note;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.lesson.Lesson;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "notes")
public class Note {

  @Id
  @GeneratedValue
  private long id;

  private String message;

  @OneToOne
  private Lesson lesson;

  public Note(String message, Lesson lesson) {
    this.message = message;
    this.lesson = lesson;
  }
}
