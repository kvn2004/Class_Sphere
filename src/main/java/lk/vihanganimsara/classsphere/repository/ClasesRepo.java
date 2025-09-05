package lk.vihanganimsara.classsphere.repository;

import lk.vihanganimsara.classsphere.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClasesRepo extends JpaRepository<Course, String> {
}
