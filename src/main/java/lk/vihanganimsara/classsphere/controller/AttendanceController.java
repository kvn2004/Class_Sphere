package lk.vihanganimsara.classsphere.controller;

import lk.vihanganimsara.classsphere.dto.ApiResponse;
import lk.vihanganimsara.classsphere.dto.AttendanceDto;
import lk.vihanganimsara.classsphere.entity.AttendanceStatus;
import lk.vihanganimsara.classsphere.service.impl.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/mark")
    public ResponseEntity<ApiResponse> markAttendance(
            @RequestParam String qrCode,
            @RequestParam String sessionId,
            @RequestHeader("X-User") String performedBy) {

        attendanceService.markAttendance(qrCode, sessionId, performedBy);

        return new ResponseEntity<>(
                new ApiResponse(200, "Attendance marked successfully", true),
                HttpStatus.OK
        );
    }

    @GetMapping("/filter")
    public ApiResponse filterAttendance(
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) AttendanceStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return new ApiResponse(
                200,
                "Filtered attendance records",
                attendanceService.filterAttendance(sessionId, studentId, status, startDate, endDate)
        );
    }

    @GetMapping("/recent")
    public List<AttendanceDto> getRecentAttendances(@RequestParam int limit) {
        return attendanceService.getRecentAttendances(limit);
    }

}

