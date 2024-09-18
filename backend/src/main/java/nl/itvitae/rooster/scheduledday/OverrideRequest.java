package nl.itvitae.rooster.scheduledday;

public record OverrideRequest(String date, int classroomId, boolean adaptWeekly) {
}
