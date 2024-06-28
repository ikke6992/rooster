package nl.itvitae.rooster.lesson;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Lesson {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

//  @OneToMany
//  private Set<Group> groups;

  private boolean isHoliday;

  private boolean isSelfStudy;
}
