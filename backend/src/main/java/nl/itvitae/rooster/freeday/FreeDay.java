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

  private String name;

  private LocalDate date;

  public FreeDay(LocalDate date, String name) {
    this.date = date;
    this.name = name;
  }
}
