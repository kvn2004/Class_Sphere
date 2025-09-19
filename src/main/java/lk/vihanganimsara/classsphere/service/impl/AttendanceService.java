package lk.vihanganimsara.classsphere.service.impl;

import jakarta.transaction.Transactional;
import lk.vihanganimsara.classsphere.dto.AttendanceDto;
import lk.vihanganimsara.classsphere.entity.Attendance;
import lk.vihanganimsara.classsphere.entity.AttendanceStatus;
import lk.vihanganimsara.classsphere.entity.CourseSession;
import lk.vihanganimsara.classsphere.entity.Student;
import lk.vihanganimsara.classsphere.repository.AttendanceRepo;
import lk.vihanganimsara.classsphere.repository.ClassSessionsRepo;
import lk.vihanganimsara.classsphere.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final StudentRepo studentRepo;
    private final ClassSessionsRepo sessionRepo;
    private final AttendanceRepo attendanceRepo;
    private final EmailService emailService;

    @Transactional
    public void markAttendance(String qrCode, String sessionId, String performedBy) {
        // 1. Find student by QR
        Student student = studentRepo.findByQrCodeContent(qrCode)
                .orElseThrow(() -> new RuntimeException("Invalid QR code"));

        // 2. Find session
        CourseSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // 3. Validate session date
        if (!session.getSessionDate().isEqual(today)) {
            throw new RuntimeException("Session date does not match today");
        }

        // 4. Validate session time
        if (now.isBefore(session.getStartTime().minusMinutes(15))) {
            throw new RuntimeException("Too early to mark attendance for this session");
        }

        if (now.isAfter(session.getEndTime())) {
            throw new RuntimeException("Session already ended");
        }

        // 5. Prevent duplicate marking
        if (attendanceRepo.existsByStudentAndSession(student, session)) {
            throw new RuntimeException("Attendance already marked");
        }

        // 6. Decide attendance status
        AttendanceStatus status;
        if (now.isBefore(session.getStartTime().plusMinutes(20))) {
            status = AttendanceStatus.PRESENT;
        } else if (now.isBefore(session.getEndTime())) {
            status = AttendanceStatus.LATE; // after 10 mins, before end
        } else {
            status = AttendanceStatus.ABSENT;
        }
        // 7. Save attendance
        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setSession(session);
        attendance.setStatus(status);
        attendanceRepo.save(attendance);

        log.info("Attendance [{}] marked: Student {} for session {}", status, student.getId(), sessionId);

        // 8. Send email
        emailService.sendAttendanceEmails(student, session, status);

        // 9. Audit
        log.info("Audit: Attendance marked by {}", performedBy);
    }

    public List<AttendanceDto> filterAttendance(String sessionId, String studentId,
                                                AttendanceStatus status,
                                                LocalDateTime startDate,
                                                LocalDateTime endDate) {
        return attendanceRepo.searchAttendance(sessionId, studentId, status, startDate, endDate)
                .stream()
                .map(a -> new AttendanceDto(
                        a.getAttendanceId(),
                        a.getSession().getSessionId(),
                        a.getStudent().getId(),
                        a.getStatus(),
                        a.getMarkedAt()
                ))
                .toList();
    }


    public List<AttendanceDto> getRecentAttendances(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        return attendanceRepo.findRecentAttendances(pageable)
                .stream()
                .map(a -> {
                    AttendanceDto dto = new AttendanceDto();
                    dto.setAttendanceId(a.getAttendanceId());
                    dto.setSessionId(a.getSession() != null ? a.getSession().getSessionId() : null);
                    dto.setStudentId(a.getStudent() != null ? a.getStudent().getId() : null);
                    dto.setStatus(a.getStatus()); // enum
                    dto.setMarkedAt(a.getMarkedAt());
                    return dto;
                })
                .toList();
    }

}

