package lk.vihanganimsara.classsphere.repository;

import lk.vihanganimsara.classsphere.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepo extends JpaRepository<Teacher,Long> {
    Optional<Teacher> findById(String id);
}
