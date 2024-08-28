package nl.itvitae.rooster.scheduledday;

import java.time.LocalDate;

public record ScheduleddayDTO(Long id, LocalDate date, Long classroomId, int groupNumber,
                              String groupColour, String field, String teacher) {

  public ScheduleddayDTO(Scheduledday scheduledday) {
    this(scheduledday.getId(), scheduledday.getDate(), scheduledday.getClassroom().getId(),
        scheduledday.getLesson().getGroup().getGroupNumber(),
        scheduledday.getLesson().getGroup().getColor(),
        scheduledday.getLesson().getGroup().getField().getName(),
        scheduledday.getLesson().getTeacher().getName());
  }

}
