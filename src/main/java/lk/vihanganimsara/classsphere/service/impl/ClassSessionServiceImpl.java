package lk.vihanganimsara.classsphere.service.impl;

import lk.vihanganimsara.classsphere.dto.CourseSessionDto;
import lk.vihanganimsara.classsphere.entity.CourseSession;
import lk.vihanganimsara.classsphere.repository.ClasesRepo;
import lk.vihanganimsara.classsphere.repository.ClassSessionsRepo;
import lk.vihanganimsara.classsphere.repository.HallRepo;
import lk.vihanganimsara.classsphere.service.AuditLogsService;
import lk.vihanganimsara.classsphere.service.ClassSessionsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor

public class ClassSessionServiceImpl implements ClassSessionsService {
    private final ClassSessionsRepo sessionRepo;
    private final ClasesRepo courseRepo;
    private final HallRepo hallRepo;
    final ModelMapper modelMapper;
    private final AuditLogsService auditLogsService;

    @Override
    public void save(CourseSessionDto dto) {
        CourseSession session = new CourseSession();
        session.setCourse(courseRepo.findById(dto.getCourseId()).orElse(null));
        session.setSessionDate(dto.getSessionDate());
        session.setStartTime(dto.getStartTime());
        session.setEndTime(dto.getEndTime());
        session.setHall(hallRepo.findById(dto.getHallId()).orElse(null));
        session.setNotes(dto.getNotes());
        sessionRepo.save(session);

        auditLogsService.logAction(
                "Course/Clases",
                session.getSessionId(),
                "CREATE",
                "Created Session: " + session.getCourse().getTitle()
        );
    }

    @Override
    public void update(CourseSessionDto dto) {
        CourseSession session = sessionRepo.findById(dto.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setCourse(courseRepo.findById(dto.getCourseId()).orElse(null));
        session.setSessionDate(dto.getSessionDate());
        session.setStartTime(dto.getStartTime());
        session.setEndTime(dto.getEndTime());
        session.setHall(hallRepo.findById(dto.getHallId()).orElse(null));
        session.setNotes(dto.getNotes());
        sessionRepo.save(session);

        auditLogsService.logAction(
                "Course/Clases",
                session.getSessionId(),
                "UPDATE",
                "Updated Session: " + session.getCourse().getTitle()
        );
    }

    @Override
    public void delete(String id) {

        sessionRepo.deleteById(id);
        auditLogsService.logAction(
                "Course/Clases",
                id,
                "DELETE",
                "Deleted Session: " + id
        );
    }

    @Override
    public List<CourseSessionDto> findAllByCourse(String courseId) {
        List<CourseSession> all = sessionRepo.findAll();
        return modelMapper.map(all, new TypeToken<List<CourseSessionDto>>() {
        }.getType());
    }
}
