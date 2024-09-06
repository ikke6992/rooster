package nl.itvitae.rooster.scheduledday;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import nl.itvitae.rooster.classroom.Classroom;
import nl.itvitae.rooster.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleddayRepository extends JpaRepository<Scheduledday, Long> {
  List<Scheduledday> findByDateBetween(LocalDate startDate, LocalDate endDate);
  boolean existsByDateAndClassroom(LocalDate date, Classroom classroom);

  boolean existsByDateAndLessonGroup(LocalDate date, Group group);

  Optional<Scheduledday> findByDateAndClassroom(LocalDate date, Classroom classroom);

  List<Scheduledday> findByDate(LocalDate date);

  List<Scheduledday> findByLessonGroup(Group group);
}
