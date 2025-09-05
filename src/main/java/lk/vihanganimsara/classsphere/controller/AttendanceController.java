package lk.vihanganimsara.classsphere.controller;

import lk.vihanganimsara.classsphere.dto.ApiResponse;
import lk.vihanganimsara.classsphere.service.impl.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
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
}

