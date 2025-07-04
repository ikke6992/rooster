package nl.itvitae.rooster.teacher;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.MyDay;
import nl.itvitae.rooster.group.Group;
import nl.itvitae.rooster.lesson.ArchivedLesson;
import nl.itvitae.rooster.lesson.Lesson;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="teachers")
public class Teacher {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @OneToMany(mappedBy = "teacher")
  private List<GroupTeacher> groupTeachers = new ArrayList<>();
  @OneToMany(mappedBy = "teacher")
  private List<ArchivedGroupTeacher> archivedGroupTeachers = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "teacher_day",
      joinColumns = @JoinColumn(name = "teacher_id"),
      inverseJoinColumns = @JoinColumn(name = "day_id"))
  private List<MyDay> availability;

  private int maxDaysPerWeek;

  @OneToMany
  private List<Lesson> lessons = new ArrayList<>();
  @OneToMany
  private List<ArchivedLesson> archivedLessons = new ArrayList<>();

  public Teacher(String name, List<MyDay> availability, int maxDaysPerWeek) {
    this.name = name;
    this.availability = availability;
    this.maxDaysPerWeek = maxDaysPerWeek;
  }

  public void addGroupTeacher(GroupTeacher groupTeacher) {
    groupTeachers.add(groupTeacher);
  }

  public void removeGroupTeacher(GroupTeacher groupTeacher) {
    groupTeachers.remove(groupTeacher);
  }

  public void addLesson(Lesson lesson) {
    lessons.add(lesson);
  }

  public void removeLesson(Lesson lesson) {
    lessons.remove(lesson);
  }

  public void addArchivedLesson(ArchivedLesson lesson) {
    archivedLessons.add(lesson);
  }
}
