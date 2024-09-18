package nl.itvitae.rooster.scheduledday;

import java.util.List;
import java.util.Map;

public record OverrideDTO(List<ScheduleddayDTO> successes, Map<ScheduleddayDTO, String> failures) {
}
