package lk.vihanganimsara.classsphere.service;



import lk.vihanganimsara.classsphere.dto.EnrollmentDto;

import java.util.List;

public interface EnrollmentService {
    void enrollStudent(EnrollmentDto dto);
    List<EnrollmentDto> getEnrollmentsByStudent(String studentId);
    List<EnrollmentDto> getEnrollmentsByCourse(String courseId);
}

