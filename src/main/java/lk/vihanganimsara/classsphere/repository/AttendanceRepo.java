package lk.vihanganimsara.classsphere.repository;

import lk.vihanganimsara.classsphere.entity.Attendance;
import lk.vihanganimsara.classsphere.entity.AttendanceStatus;
import lk.vihanganimsara.classsphere.entity.CourseSession;
import lk.vihanganimsara.classsphere.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepo extends JpaRepository<Attendance, Integer> {
    boolean existsByStudentAndSession(Student student, CourseSession session);

    // ✅ Find by session

    List<Attendance> findBySession_SessionId(String sessionId);

    // ✅ Find by student
    List<Attendance> findByStudent_Id(String studentId);

    // ✅ Find by status
    List<Attendance> findByStatus(AttendanceStatus status);

    // ✅ Find by session + status
    List<Attendance> findBySession_SessionIdAndStatus(String sessionId, AttendanceStatus status);

    // ✅ Find by student + status
    List<Attendance> findByStudent_IdAndStatus(String studentId, AttendanceStatus status);

    // ✅ Find by date range
    List<Attendance> findByMarkedAtBetween(LocalDateTime start, LocalDateTime end);

    // ✅ Custom query for combinations (optional)
    @Query("SELECT a FROM Attendance a " +
            "WHERE (:sessionId IS NULL OR TRIM (a.session.sessionId) = :sessionId) " +
            "AND (:studentId IS NULL OR a.student.id = :studentId) " +
            "AND (:status IS NULL OR a.status = :status) " +
            "AND (:startDate IS NULL OR a.markedAt >= :startDate) " +
            "AND (:endDate IS NULL OR a.markedAt <= :endDate)")
    List<Attendance> searchAttendance(
            @Param("sessionId") String sessionId,
            @Param("studentId") String studentId,
            @Param("status") AttendanceStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT a FROM Attendance a ORDER BY a.markedAt DESC")
    List<Attendance> findRecentAttendances(Pageable pageable);

}
