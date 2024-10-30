package nl.itvitae.rooster.group.vacation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.group.ArchivedGroup;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="archived_vacations")
public class ArchivedVacation {

  @Id
  private Long id;

  private LocalDate startDate;
  private int weeks;

  @ManyToOne
  private ArchivedGroup group;

  public ArchivedVacation(Vacation vacation, ArchivedGroup group) {
    this.id = vacation.getId();
    this.startDate = vacation.getStartDate();
    this.weeks = vacation.getWeeks();
    this.group = group;
  }
}
