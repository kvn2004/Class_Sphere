package lk.vihanganimsara.classsphere.service;

import lk.vihanganimsara.classsphere.dto.HallDto;
import lk.vihanganimsara.classsphere.dto.StudentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HallService {
    public void save(HallDto hallDto) ;
    public void update(HallDto hallDto) ;
    List<HallDto> getAll();
    void deleteById(String id);
}
