package nl.itvitae.rooster.group;

public record GroupRequest(int groupNumber, String color, int numberOfStudents, long field, String startDate, int weeksPhase1, int weeksPhase2, int weeksPhase3) {
}
