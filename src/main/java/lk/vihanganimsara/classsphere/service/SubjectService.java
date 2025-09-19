package lk.vihanganimsara.classsphere.service;

import lk.vihanganimsara.classsphere.dto.StudentDto;
import lk.vihanganimsara.classsphere.dto.SubjectDto;
import lk.vihanganimsara.classsphere.dto.getSubjectDto;

import java.util.List;

public interface SubjectService {
    void save(SubjectDto subject);
    void update(SubjectDto subject);
    void delete(String id);
    List<getSubjectDto> getAll();

}
