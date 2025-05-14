package nl.itvitae.rooster.teacher;

import nl.itvitae.rooster.MyDay;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public record TeacherDTO(long id, String name, List<DayOfWeek> availability, int maxDaysPerWeek, List<String> groups) {

  static TeacherDTO of(Teacher teacher) {
    long id = teacher.getId();
    String name = teacher.getName();
    List<DayOfWeek> availability = new ArrayList<>();
    for (MyDay day : teacher.getAvailability()) {
      availability.add(day.getDay());
    }
    int maxDaysPerWeek = teacher.getMaxDaysPerWeek();
    List<String> groups = new ArrayList<>();
    for (GroupTeacher groupTeacher : teacher.getGroupTeachers()) {
      String groupInfo = String.format("Group %d:\n\t- %d days phase 1\n\t- %d days phase 2\n\t- %d days phase 3",
          groupTeacher.getGroup().getGroupNumber(), groupTeacher.getDaysPhase1(),
          groupTeacher.getDaysPhase2(), groupTeacher.getDaysPhase3());
      groups.add(groupInfo);
    }
    return new TeacherDTO(id, name, availability, maxDaysPerWeek, groups);
  }
}
