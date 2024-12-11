package nl.itvitae.rooster.scheduledday;

import java.util.List;

public record OverrideDTO(List<String> successes, List<String> failures) {
}
