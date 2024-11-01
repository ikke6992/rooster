package nl.itvitae.rooster.teacher;

public record TeacherRequest(String name, String[] availability, int maxDaysPerWeek) {
}
