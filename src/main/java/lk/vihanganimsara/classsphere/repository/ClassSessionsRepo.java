package lk.vihanganimsara.classsphere.repository;

import lk.vihanganimsara.classsphere.entity.CourseSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ClassSessionsRepo extends JpaRepository<CourseSession, String> {
    List<CourseSession> findBySessionDateBetween(LocalDate start, LocalDate end);
    List<CourseSession> findBySessionDate(LocalDate date);
}
