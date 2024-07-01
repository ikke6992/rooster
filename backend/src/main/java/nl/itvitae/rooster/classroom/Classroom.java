package nl.itvitae.rooster.classroom;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Classroom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  // will also be used as room number
  private Long id;

  private byte capacity;

  private boolean hasBeamer;

  private boolean forPracticum;
}
