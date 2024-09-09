package nl.itvitae.rooster.teacher;

public record TeacherRequest(String name, boolean teachesPracticum, String[] availability, int maxDaysPerWeek) {
}
