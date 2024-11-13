package nl.itvitae.rooster.group.vacation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.group.Group;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="vacations")
public class Vacation {

  @Id
  @GeneratedValue
  private Long id;

  private LocalDate startDate;
  private int weeks;

  @ManyToOne
  @JsonBackReference
  private Group group;

  public Vacation(LocalDate startDate, int weeks, Group group) {
    this.startDate = startDate;
    this.weeks = weeks;
    this.group = group;
  }
}
