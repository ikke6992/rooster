package nl.itvitae.rooster.group;

import nl.itvitae.rooster.group.vacation.ArchivedVacation;
import nl.itvitae.rooster.group.vacation.Vacation;

public record GroupDTO(
    int groupNumber, String color, int numberOfStudents, String field,
    String startDate, int weeksPhase1, int weeksPhase2, int weeksPhase3, int daysPhase1, int daysPhase2, int daysPhase3,
    String[] teachers, String[] vacations) {

  public static GroupDTO of(Group group) {
    int groupNumber = group.getGroupNumber();
    String color = group.getColor();
    int numberOfStudents = group.getNumberOfStudents();
    String field = group.getField();
    String startDate = group.getStartDate().toString();
    int weeksPhase1 = group.getWeeksPhase1();
    int weeksPhase2 = group.getWeeksPhase2();
    int weeksPhase3 = group.getWeeksPhase3();
    int daysPhase1 = group.getDaysPhase1();
    int daysPhase2 = group.getDaysPhase2();
    int daysPhase3 = group.getDaysPhase3();
    String[] teachers = new String[group.getGroupTeachers().size()];
    for (int i = 0; i < teachers.length; i++) {
      teachers[i] = group.getGroupTeachers().get(i).getTeacher().getName();
    }
    String[] vacations = new String[group.getVacations().size()];
    for (int i = 0; i < vacations.length; i++) {
      Vacation vacation = group.getVacations().get(i);
      vacations[i] = String.format(
          "%d weeks vacation starting %s", vacation.getWeeks(), vacation.getStartDate().toString());
    }
    return new GroupDTO(groupNumber, color, numberOfStudents, field,
        startDate, weeksPhase1, weeksPhase2, weeksPhase3, daysPhase1, daysPhase2, daysPhase3, teachers, vacations);
  }

  static GroupDTO ofArchived(ArchivedGroup group) {
    int groupNumber = group.getGroupNumber();
    String color = group.getColor();
    int numberOfStudents = group.getNumberOfStudents();
    String field = group.getField();
    String startDate = group.getStartDate().toString();
    int weeksPhase1 = group.getWeeksPhase1();
    int weeksPhase2 = group.getWeeksPhase2();
    int weeksPhase3 = group.getWeeksPhase3();
    int daysPhase1 = group.getDaysPhase1();
    int daysPhase2 = group.getDaysPhase2();
    int daysPhase3 = group.getDaysPhase3();
    String[] teachers = new String[group.getGroupTeachers().size()];
    for (int i = 0; i < teachers.length; i++) {
      teachers[i] = group.getGroupTeachers().get(i).getTeacher().getName();
    }
    String[] vacations = new String[group.getVacations().size()];
    for (int i = 0; i < vacations.length; i++) {
      ArchivedVacation vacation = group.getVacations().get(i);
      vacations[i] = String.format(
          "%d weeks vacation starting %s", vacation.getWeeks(), vacation.getStartDate().toString());
    }
    return new GroupDTO(groupNumber, color, numberOfStudents, field,
        startDate, weeksPhase1, weeksPhase2, weeksPhase3, daysPhase1, daysPhase2, daysPhase3, teachers, vacations);

  }
}
