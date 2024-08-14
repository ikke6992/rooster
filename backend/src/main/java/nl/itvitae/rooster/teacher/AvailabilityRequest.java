package nl.itvitae.rooster.teacher;

public record AvailabilityRequest(boolean[] availability, int maxDays) {
}
