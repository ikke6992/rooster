package nl.itvitae.rooster.group;

public record GroupRequest(int groupNumber, String color, int numberOfStudents, String field,
                           String startDate, int daysPhase1, int weeksPhase1, int daysPhase2, int weeksPhase2,
                           int daysPhase3, int weeksPhase3, AssignmentRequest[] teacherAssignments) {
}
