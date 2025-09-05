package lk.vihanganimsara.classsphere.service.impl;

import jakarta.transaction.Transactional;
import lk.vihanganimsara.classsphere.dto.CourseDto;
import lk.vihanganimsara.classsphere.dto.HallDto;
import lk.vihanganimsara.classsphere.entity.AuditLog;
import lk.vihanganimsara.classsphere.entity.Course;
import lk.vihanganimsara.classsphere.entity.Hall;
import lk.vihanganimsara.classsphere.repository.*;
import lk.vihanganimsara.classsphere.service.AuditLogsService;
import lk.vihanganimsara.classsphere.service.ClasesService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClasesServiceImpl implements ClasesService {
    private final ClasesRepo courseRepo;
    private final SubjectRepo subjectRepo;
    private final TeacherRepo teacherRepo;
    private final HallRepo hallRepo;
    final ModelMapper modelMapper;
    private final AuditLogsService auditLogsService;

    @Override
    public void save(CourseDto dto) {
        Course course = new Course();
        course.setSubject(subjectRepo.findById(dto.getSubjectId()).orElse(null));
        course.setTeacher(teacherRepo.findById(dto.getTeacherId()).orElse(null));
        course.setTitle(dto.getTitle());
        course.setDefaultHall(hallRepo.findById(dto.getDefaultHallId()).orElse(null));
        course.setStartMonth(dto.getStartMonth());
        course.setActive(dto.getActive());
        courseRepo.save(course);

        auditLogsService.logAction(
                "Course/Clases",
                course.getCourseId(),
                "CREATE",
                "Created subject: " + course.getTitle()
        );

    }

    @Override
    public void update(CourseDto dto) {
        Course course = courseRepo.findById(dto.getCourseId()).orElseThrow(() -> new RuntimeException("Course not found"));

        course.setSubject(subjectRepo.findById(dto.getSubjectId()).orElse(null));
        course.setTeacher(teacherRepo.findById(dto.getTeacherId()).orElse(null));
        course.setTitle(dto.getTitle());
        course.setDefaultHall(hallRepo.findById(dto.getDefaultHallId()).orElse(null));
        course.setStartMonth(dto.getStartMonth());
        course.setActive(dto.getActive());

        courseRepo.save(course);

        auditLogsService.logAction(
                "Course/Clases",
                course.getCourseId(),
                "UPDATE",
                "Update subject: " + course.getTitle()
        );
    }

    @Override
    public List<CourseDto> getAll() {
        List<Course> all = courseRepo.findAll();
        return modelMapper.map(all, new TypeToken<List<CourseDto>>() {
        }.getType());
    }

    @Override
    public void deleteById(String id) {
        courseRepo.deleteById(id);
        auditLogsService.logAction(
                "Course/Clases",
                id,
                "CREATE",
                "Deleted subject: " + id
        );
    }
}
