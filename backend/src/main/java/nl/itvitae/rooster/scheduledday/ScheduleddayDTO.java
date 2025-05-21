package nl.itvitae.rooster.scheduledday;

import java.time.LocalDate;

public record ScheduleddayDTO(Long id, LocalDate date, Long classroomId, int groupNumber,
                              String groupColor, String field, String teacher, String note, boolean isExam) {

  public ScheduleddayDTO(Scheduledday scheduledday) {
    this(scheduledday.getId(), scheduledday.getDate(), scheduledday.getClassroom().getId(),
        scheduledday.getLesson().getGroup().getGroupNumber(),
        scheduledday.getLesson().getGroup().getColor(),
        scheduledday.getLesson().getGroup().getField(),
        getTeacherName(scheduledday), scheduledday.getLesson().getNote(), scheduledday.getLesson().isExam());
  }

  public static String getTeacherName(Scheduledday scheduledday) {
    String teacher = "none";
    if (scheduledday.getLesson().getTeacher() != null) {
      teacher = scheduledday.getLesson().getTeacher().getName();
    }
    return teacher;
  }

}
