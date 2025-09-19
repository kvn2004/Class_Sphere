package lk.vihanganimsara.classsphere.service.impl;

import jakarta.transaction.Transactional;
import lk.vihanganimsara.classsphere.dto.SubjectDto;
import lk.vihanganimsara.classsphere.dto.TeacherDto;
import lk.vihanganimsara.classsphere.dto.getSubjectDto;
import lk.vihanganimsara.classsphere.entity.AuditLog;
import lk.vihanganimsara.classsphere.entity.Subject;
import lk.vihanganimsara.classsphere.entity.Teacher;
import lk.vihanganimsara.classsphere.repository.AuditLogRepo;
import lk.vihanganimsara.classsphere.repository.SubjectRepo;
import lk.vihanganimsara.classsphere.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepo subjectRepo;
    private final AuditLogRepo auditLogRepo;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void save(SubjectDto dto) {

        Subject subject = new Subject();
        subject.setSubjectName(dto.getSubjectName());
        subject.setGradeLevel(dto.getGradeLevel());

        subjectRepo.save(subject);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "SYSTEM";
        AuditLog log = new AuditLog();
        log.setEntityName("Subject");
        log.setEntityId(String.valueOf(subject.getSubjectId()));
        log.setAction("SAVE");
        log.setPerformedBy(currentUser);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepo.save(log);

    }

    @Override
    @Transactional
    public void update(SubjectDto dto) {

        Optional<Subject> byId = subjectRepo.findById(dto.getSubjectId());

        if (!byId.isPresent()) {
            log.warn("Subject with id {} not found", dto.getSubjectId());
            throw new RuntimeException("Subject with id " + dto.getSubjectId() + " not found");
        }
        Subject subject = byId.get();
        subject.setSubjectName(dto.getSubjectName());
        subject.setGradeLevel(dto.getGradeLevel());
        subjectRepo.save(subject);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "SYSTEM";
        AuditLog log = new AuditLog();
        log.setEntityName("Subject");
        log.setEntityId(String.valueOf(subject.getSubjectId()));
        log.setAction("UPDATE");
        log.setPerformedBy(currentUser);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepo.save(log);
    }

    @Override
    public void delete(String id) {
        subjectRepo.deleteById(id);

    }

    @Override
    public List<getSubjectDto> getAll() {
        List<Subject> all = subjectRepo.findAll();
        return all.stream()
                .map(s -> new getSubjectDto(s.getSubjectId(), s.getSubjectName(), s.getGradeLevel()))
                .toList();
    }
}
