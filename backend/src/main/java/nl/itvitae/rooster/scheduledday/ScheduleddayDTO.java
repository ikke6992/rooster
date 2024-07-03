package nl.itvitae.rooster.scheduledday;

import java.time.LocalDate;

public record ScheduleddayDTO(Long id, LocalDate date, Long classroom_id) {
  public ScheduleddayDTO(Scheduledday scheduledday) {
    this(scheduledday.getId(), scheduledday.getDate(), scheduledday.getClassroom().getId());
  }

}
