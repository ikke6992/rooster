package nl.itvitae.rooster.freeday;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class FreeDay {

  @Id
  @GeneratedValue
  private Long id;

  private LocalDate freeDay;

  public FreeDay(LocalDate freeDay) {
    this.freeDay = freeDay;
  }
}
