package lk.vihanganimsara.classsphere.service;

import lk.vihanganimsara.classsphere.dto.StudentDto;
import lk.vihanganimsara.classsphere.entity.Student;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    public boolean save(StudentDto studentDto, MultipartFile photo) throws Exception;
    public void update(StudentDto studentDto, MultipartFile photo) throws Exception;
    List<StudentDto> getAll();
    void deleteById(String id);
}
