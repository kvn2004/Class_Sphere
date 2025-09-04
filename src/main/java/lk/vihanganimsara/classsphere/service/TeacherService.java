package lk.vihanganimsara.classsphere.service;

import lk.vihanganimsara.classsphere.dto.StudentDto;
import lk.vihanganimsara.classsphere.dto.TeacherDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherService {
    public boolean save(TeacherDto teacherDto, MultipartFile photo) throws Exception;
    public void update(TeacherDto teacherDto, MultipartFile photo) throws Exception;
    List<StudentDto> getAll();
    void deleteById(String id);
}
