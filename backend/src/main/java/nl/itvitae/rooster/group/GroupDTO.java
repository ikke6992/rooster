package nl.itvitae.rooster.group;

public record GroupDTO(int groupNumber, String color, int numberOfStudents, String field, String startDate, int weeksPhase1, int weeksPhase2, int weeksPhase3, int daysPhase1, int daysPhase2, int daysPhase3, String[] teachers) {

  static GroupDTO of(Group group) {
    int groupNumber = group.getGroupNumber();
    String color = group.getColor();
    int numberOfStudents = group.getNumberOfStudents();
    String field = group.getField().getName();
    String startDate = group.getStartDate().toString();
    int weeksPhase1 = group.getWeeksPhase1();
    int weeksPhase2 = group.getWeeksPhase2();
    int weeksPhase3 = group.getWeeksPhase3();
    int daysPhase1 = group.getField().getDaysPhase1();
    int daysPhase2 = group.getField().getDaysPhase2();
    int daysPhase3 = group.getField().getDaysPhase3();
    String[] teachers = new String[group.getTeachers().size()];
    for (int i = 0; i < teachers.length; i++) {
      teachers[i] = group.getTeachers().get(i).getName();
    }
    return new GroupDTO(groupNumber, color, numberOfStudents, field, startDate, weeksPhase1, weeksPhase2, weeksPhase3, daysPhase1, daysPhase2, daysPhase3, teachers);
  }
}
