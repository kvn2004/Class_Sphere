package lk.vihanganimsara.classsphere.repository;

import lk.vihanganimsara.classsphere.entity.CourseSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassSessionsRepo extends JpaRepository<CourseSession, String> {
}
