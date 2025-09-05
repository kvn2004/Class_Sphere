package lk.vihanganimsara.classsphere.service;

import lk.vihanganimsara.classsphere.dto.CourseDto;
import lk.vihanganimsara.classsphere.dto.HallDto;
import lk.vihanganimsara.classsphere.dto.StudentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClasesService {
    public void save(CourseDto dto) ;
    public void update(CourseDto dto) ;
    List<CourseDto> getAll();
    void deleteById(String id);
}
