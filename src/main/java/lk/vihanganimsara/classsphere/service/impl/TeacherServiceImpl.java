package lk.vihanganimsara.classsphere.service.impl;

import jakarta.transaction.Transactional;
import lk.vihanganimsara.classsphere.dto.StudentDto;
import lk.vihanganimsara.classsphere.dto.TeacherDto;
import lk.vihanganimsara.classsphere.entity.AuditLog;
import lk.vihanganimsara.classsphere.entity.Student;
import lk.vihanganimsara.classsphere.entity.Teacher;
import lk.vihanganimsara.classsphere.repository.AuditLogRepo;
import lk.vihanganimsara.classsphere.repository.TeacherRepo;
import lk.vihanganimsara.classsphere.service.TeacherService;
import lk.vihanganimsara.classsphere.util.QrGenaratorForStudent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepo  teacherRepo;
    private final AuditLogRepo auditLogRepo;
    private final String qrDirectory = "qrcodes/TechersQr"; // folder relative to working dir
    private final ModelMapper modelMapper;

    @Override
    public boolean save(TeacherDto teacherDto, MultipartFile photo) throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName(teacherDto.getName());
        teacher.setEmail(teacherDto.getEmail());
        teacher.setAddress(teacherDto.getAddress());
        teacher.setPhone(teacherDto.getPhone());

        // unique QR content
        String qrContent = UUID.randomUUID().toString();
        teacher.setQrCodeContent(qrContent);

        Teacher saved = teacherRepo.save(teacher);

        //  Ensure QR folder exists
        File dir = new File(qrDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //  Generate QR file using student ID for filename
        String fileName = "teacher_" + saved.getId() + ".png";
        Path qrFilePath = Paths.get(qrDirectory, fileName);

        String qrPath = QrGenaratorForStudent.generateQRCodeImage(qrContent, qrFilePath.toString());
        saved.setQrCodePath(qrPath);


        // --- Photo ---
        if (photo != null && !photo.isEmpty()) {
            File photoDir = new File("photos/teachers-photos/");
            if (!photoDir.exists()) photoDir.mkdirs();
            String photoFileName = "teacher_" + saved.getId() + "_" + photo.getOriginalFilename();
            Path photoPath = Paths.get(photoDir.getAbsolutePath(), photoFileName);
            photo.transferTo(photoPath); // save file
            saved.setPhotoPath(photoPath.toString());
        }
        //  Update student with QR path
        teacherRepo.save(saved);
        return true;
    }

    @Override
    public void update(TeacherDto teacherDto, MultipartFile photo) throws Exception {
        // 1. Find existing student
        Optional<Teacher> optionalTeacher = teacherRepo.findById(teacherDto.getId());
        if (!optionalTeacher.isPresent()) {
            throw new RuntimeException("Student not found with ID: " + teacherDto.getId());
        }

        Teacher teacher = optionalTeacher.get();

        // 2. Update fields
        teacher.setName(teacherDto.getName());
        teacher.setEmail(teacherDto.getEmail());
        teacher.setAddress(teacherDto.getAddress());
        teacher.setPhone(teacherDto.getPhone());


        // 4. Update photo if new one uploaded
        if (photo != null && !photo.isEmpty()) {
            File photoDir = new File("photos/teachers-photos/");
            if (!photoDir.exists()) photoDir.mkdirs();

            String photoFileName = "Teacher_" + teacher.getId() + "_" + photo.getOriginalFilename();
            Path photoPath = Paths.get(photoDir.getAbsolutePath(), photoFileName);

            photo.transferTo(photoPath); // overwrite old photo
            teacher.setPhotoPath(photoPath.toString());
        }

        // 5. Save updated student
        teacherRepo.save(teacher);
    }

    @Override
    public List<StudentDto> getAll() {
        List<Teacher> all = teacherRepo.findAll();
        return modelMapper.map(all, new TypeToken<List<TeacherDto>>() {
        }.getType());
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Teacher teacher = teacherRepo.findById(id).get();
        // studentRepository.delete(student);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "SYSTEM";

        teacher.setDeleted(true);
        teacher.setDeletedAt(LocalDateTime.now());
        teacher.setDeletedBy(currentUser);
        teacherRepo.save(teacher);

        AuditLog log = new AuditLog();
        log.setEntityName("TEACHER");
        log.setEntityId(teacher.getId());
        log.setAction("DELETE");
        log.setPerformedBy(currentUser);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepo.save(log);

    }
}
