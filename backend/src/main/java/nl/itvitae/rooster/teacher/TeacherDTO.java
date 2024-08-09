package nl.itvitae.rooster.teacher;

import nl.itvitae.rooster.MyDay;
import nl.itvitae.rooster.group.Group;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public record TeacherDTO(String name, List<DayOfWeek> availability, int maxDaysPerWeek, List<String> groups) {

  static TeacherDTO of(Teacher teacher) {
    String name = teacher.getName();
    List<DayOfWeek> availability = new ArrayList<>();
    for (MyDay day : teacher.getAvailability()) {
      availability.add(day.getDay());
    }
    int maxDaysPerWeek = teacher.getMaxDaysPerWeek();
    List<String> groups = new ArrayList<>();
    for (Group group : teacher.getGroups()) {
      String groupName = "Group " + group.getGroupNumber() + " " + group.getField().getName();
      groups.add(groupName);
    }
    return new TeacherDTO(name, availability, maxDaysPerWeek, groups);
  }
}
