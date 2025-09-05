package lk.vihanganimsara.classsphere.service;

import lk.vihanganimsara.classsphere.dto.CourseSessionDto;

import java.util.List;

public interface ClassSessionsService {
    void save(CourseSessionDto dto);
    void update(CourseSessionDto dto);
    void delete(String id);



    //CourseSessionDto findById(Integer id);
    List<CourseSessionDto> findAllByCourse(String courseId);
}
