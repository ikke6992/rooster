package nl.itvitae.rooster.lesson;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.scheduledday.Scheduledday;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Lesson {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private Scheduledday scheduledday;

//  @ManyToOne
//  private Group group;
//
//  @ManyToOne
//  private Teacher teacher;

  private boolean isPracticum;
}
