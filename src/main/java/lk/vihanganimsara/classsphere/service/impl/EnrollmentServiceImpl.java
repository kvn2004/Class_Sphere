package lk.vihanganimsara.classsphere.service.impl;

import lk.vihanganimsara.classsphere.dto.EnrollmentDto;
import lk.vihanganimsara.classsphere.entity.Enrollment;
import lk.vihanganimsara.classsphere.entity.EnrollmentId;
import lk.vihanganimsara.classsphere.repository.ClasesRepo;
import lk.vihanganimsara.classsphere.repository.EnrollmentRepo;
import lk.vihanganimsara.classsphere.repository.StudentRepo;
import lk.vihanganimsara.classsphere.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepo enrollmentRepo;
    private final StudentRepo studentRepo;
    private final ClasesRepo courseRepo;

    @Override
    public void enrollStudent(EnrollmentDto dto) {
        Enrollment enrollment = new Enrollment();
        EnrollmentId id = new EnrollmentId(dto.getStudentId(), dto.getCourseId());

        enrollment.setId(id);
        enrollment.setStudent(studentRepo.findById(dto.getStudentId()).orElse(null));
        enrollment.setCourse(courseRepo.findById(dto.getCourseId()).orElse(null));
        enrollment.setEnrolledOn(dto.getEnrolledOn() != null ? dto.getEnrolledOn() : LocalDate.now());
        enrollment.setActive(dto.getActive() != null ? dto.getActive() : true);

        enrollmentRepo.save(enrollment);
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByStudent(String studentId) {
        return enrollmentRepo.findByStudent_Id(studentId)
                .stream().map(e -> new EnrollmentDto(
                        e.getStudent().getId(),            // studentId
                        e.getStudent().getName(),          // studentName
                        e.getCourse().getCourseId(),       // courseId
                        e.getCourse().getTitle(),          // courseTitle
                        e.getEnrolledOn(),
                        e.getActive()
                ))
                .toList();
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByCourse(String courseId) {
        return enrollmentRepo.findByCourse_CourseId(courseId)
                .stream()
                .map(e -> new EnrollmentDto(
                        e.getStudent().getId(),           // studentId
                        e.getStudent().getName(),         // studentName
                        e.getCourse().getCourseId(),      // courseId
                        e.getCourse().getTitle(),         // courseTitle
                        e.getEnrolledOn(),
                        e.getActive()
                ))
                .toList();
    }

}
