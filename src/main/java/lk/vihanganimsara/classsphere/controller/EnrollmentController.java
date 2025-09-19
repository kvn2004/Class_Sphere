package lk.vihanganimsara.classsphere.controller;

import lk.vihanganimsara.classsphere.dto.ApiResponse;
import lk.vihanganimsara.classsphere.dto.EnrollmentDto;
import lk.vihanganimsara.classsphere.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    public ResponseEntity<ApiResponse> enrollStudent(@RequestBody EnrollmentDto dto) {
        enrollmentService.enrollStudent(dto);
        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), "Student enrolled successfully", null)
        );
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse> getEnrollmentsByStudent(@PathVariable String studentId) {
        List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), "Enrollments fetched successfully", enrollments)
        );
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse> getEnrollmentsByCourse(@PathVariable String courseId) {
        List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), "Enrollments fetched successfully", enrollments)
        );
    }
}

