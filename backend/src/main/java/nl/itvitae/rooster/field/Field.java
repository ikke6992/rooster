package nl.itvitae.rooster.field;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "fields")
public class Field {

  @Id
  @GeneratedValue
  private long id;

  private String name;
  private int daysPhase1;
  private int daysPhase2;
  private int daysPhase3;

  public Field(String name, int daysPhase1, int daysPhase2, int daysPhase3) {
    this.name = name;
    this.daysPhase1 = daysPhase1;
    this.daysPhase2 = daysPhase2;
    this.daysPhase3 = daysPhase3;
  }
}
