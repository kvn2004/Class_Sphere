package lk.vihanganimsara.classsphere.dto;

import lk.vihanganimsara.classsphere.entity.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDto {
    private Integer attendanceId;

    private String sessionId;
//    private String courseTitle;   // optional: show which course
//    private String hallName;      // optional: show where it was held

    private String studentId;
    private String studentName;

    private AttendanceStatus status;
    private LocalDateTime markedAt;

    public AttendanceDto(Integer attendanceId, String sessionId, String id, AttendanceStatus status, LocalDateTime markedAt) {
        this.attendanceId = attendanceId;
        this.sessionId = sessionId;
        this.studentId = id;
        this.status = status;
        this.markedAt = markedAt;

    }


}

