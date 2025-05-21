package nl.itvitae.rooster.group;

import nl.itvitae.rooster.teacher.ArchivedGroupTeacher;
import nl.itvitae.rooster.teacher.GroupTeacher;

public record AssignmentRequest(String name, long id, int daysPhase1, int daysPhase2, int daysPhase3) {

  static AssignmentRequest of(GroupTeacher teacher) {
    String name = teacher.getTeacher().getName();
    long id = teacher.getTeacher().getId();
    int daysPhase1 = teacher.getDaysPhase1();
    int daysPhase2 = teacher.getDaysPhase2();
    int daysPhase3 = teacher.getDaysPhase3();
    return new AssignmentRequest(name, id, daysPhase1, daysPhase2, daysPhase3);
  }

  static AssignmentRequest ofArchived(ArchivedGroupTeacher teacher) {
    String name = teacher.getTeacher().getName();
    long id = teacher.getTeacher().getId();
    int daysPhase1 = teacher.getDaysPhase1();
    int daysPhase2 = teacher.getDaysPhase2();
    int daysPhase3 = teacher.getDaysPhase3();
    return new AssignmentRequest(name, id, daysPhase1, daysPhase2, daysPhase3);
  }
}
