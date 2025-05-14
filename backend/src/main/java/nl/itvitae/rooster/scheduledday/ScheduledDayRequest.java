package nl.itvitae.rooster.scheduledday;

import java.time.LocalDate;

public record ScheduledDayRequest(int groupNumber, Long teacherId, Long classroomId, String date) {

}
