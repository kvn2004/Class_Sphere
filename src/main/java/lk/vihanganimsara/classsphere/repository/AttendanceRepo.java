package lk.vihanganimsara.classsphere.repository;

import lk.vihanganimsara.classsphere.entity.Attendance;
import lk.vihanganimsara.classsphere.entity.CourseSession;
import lk.vihanganimsara.classsphere.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepo extends JpaRepository<Attendance, Integer> {
    boolean existsByStudentAndSession(Student student, CourseSession session);
}
