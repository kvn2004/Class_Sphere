package lk.vihanganimsara.classsphere.repository;

import lk.vihanganimsara.classsphere.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student,Long> {
    Optional<Student> findByQrCodeContent(String qrCodeContent);

    Optional<Student> findById(String id);
}
