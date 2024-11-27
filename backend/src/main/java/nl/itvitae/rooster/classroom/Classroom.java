package nl.itvitae.rooster.classroom;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.scheduledday.Scheduledday;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "classrooms")
public class Classroom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  // will also be used as room number
  private Long id;

  private int capacity;

  @OneToMany(mappedBy = "classroom")
  @JsonBackReference
  private List<Scheduledday> scheduleddays;

  private boolean hasBeamer;

  private boolean forPracticum;

  public Classroom(int capacity, boolean hasBeamer, boolean forPracticum) {
    this.capacity = capacity;
    this.hasBeamer = hasBeamer;
    this.forPracticum = forPracticum;
  }
}
