package lk.vihanganimsara.classsphere.service.impl;

import jakarta.transaction.Transactional;
import lk.vihanganimsara.classsphere.dto.StudentDto;
import lk.vihanganimsara.classsphere.entity.AuditLog;
import lk.vihanganimsara.classsphere.entity.Student;
import lk.vihanganimsara.classsphere.repository.AuditLogRepo;
import lk.vihanganimsara.classsphere.repository.StudentRepo;
import lk.vihanganimsara.classsphere.service.StudentService;
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
public class StudentServiceImpl implements StudentService {

    private final StudentRepo studentRepository;
    private final AuditLogRepo auditLogRepo;
    private final String qrDirectory = "qrcodes/StudentsQr"; // folder relative to working dir
    private final ModelMapper modelMapper;

    @Override
    public boolean save(StudentDto dto, MultipartFile photo) throws Exception {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setTelephone(dto.getTelephone());
        student.setAddress(dto.getAddress());
        student.setGuardianName(dto.getGuardianName());
        student.setGuardianTelephone(dto.getGuardianTelephone());
        student.setGuardianEmail(dto.getGuardianEmail());

        String qrContent = UUID.randomUUID().toString();
        student.setQrCodeContent(qrContent);

        Student saved = studentRepository.save(student);

        // QR Code
        File qrDir = new File("E:/Advanced API Development/SpringBOOT/ClassSphere/qrcodes/");
        if (!qrDir.exists()) qrDir.mkdirs();

        String qrFileName = "student_" + saved.getId() + ".png";
        Path qrPath = Paths.get(qrDir.getAbsolutePath(), qrFileName);
        QrGenaratorForStudent.generateQRCodeImage(qrContent, qrPath.toString());

        // save relative path for frontend
        saved.setQrCodePath("/qrcodes/" + qrFileName);

        // Photo
        if (photo != null && !photo.isEmpty()) {
            File photoDir = new File("E:/Advanced API Development/SpringBOOT/ClassSphere/photos/student-photos/");
            if (!photoDir.exists()) photoDir.mkdirs();

            String photoFileName = "student_" + saved.getId() + "_" + photo.getOriginalFilename();
            Path photoPath = Paths.get(photoDir.getAbsolutePath(), photoFileName);
            photo.transferTo(photoPath);

            saved.setPhotoPath("/photos/student-photos/" + photoFileName);
        }

        studentRepository.save(saved);
        return true;
    }

    @Override
    public void update(StudentDto dto, MultipartFile photo) throws Exception {
        Student student = studentRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setTelephone(dto.getTelephone());
        student.setAddress(dto.getAddress());
        student.setGuardianName(dto.getGuardianName());
        student.setGuardianTelephone(dto.getGuardianTelephone());
        student.setGuardianEmail(dto.getGuardianEmail());

        // Update photo if uploaded
        if (photo != null && !photo.isEmpty()) {
            File photoDir = new File("E:/Advanced API Development/SpringBOOT/ClassSphere/photos/student-photos/");
            if (!photoDir.exists()) photoDir.mkdirs();

            String photoFileName = "student_" + student.getId() + "_" + photo.getOriginalFilename();
            Path photoPath = Paths.get(photoDir.getAbsolutePath(), photoFileName);
            photo.transferTo(photoPath);

            student.setPhotoPath("/photos/student-photos/" + photoFileName);
        }

        studentRepository.save(student);
    }


    @Override
    public List<StudentDto> getAll() {
        List<Student> all = studentRepository.findAll();
        return modelMapper.map(all, new TypeToken<List<StudentDto>>() {
        }.getType());
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Student student = studentRepository.findById(id).get();
        // studentRepository.delete(student);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "SYSTEM";

        student.setDeleted(true);
        student.setDeletedAt(LocalDateTime.now());
        student.setDeletedBy(currentUser);
        studentRepository.save(student);

        AuditLog log = new AuditLog();
        log.setEntityName("Student");
        log.setEntityId(String.valueOf(student.getId()));
        log.setAction("DELETE");
        log.setPerformedBy(currentUser);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepo.save(log);

    }
}
