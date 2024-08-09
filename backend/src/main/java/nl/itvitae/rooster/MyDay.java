package nl.itvitae.rooster;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.itvitae.rooster.teacher.Teacher;

import java.time.DayOfWeek;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="days")
public class MyDay {

  @Id
  @GeneratedValue
  private Long id;

  private DayOfWeek day;

  @ManyToMany(mappedBy="availability")
  private List<Teacher> teachers;

  public MyDay(DayOfWeek day) {
    this.day = day;
  }
}
