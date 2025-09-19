package lk.vihanganimsara.classsphere.repository;


import lk.vihanganimsara.classsphere.entity.Enrollment;
import lk.vihanganimsara.classsphere.entity.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface EnrollmentRepo extends JpaRepository<Enrollment, EnrollmentId> {
    List<Enrollment> findByStudent_Id(String studentId);

    List<Enrollment> findByCourse_CourseId(String courseId);

//    List<Enrollment> findByCourse_CourseId(String courseId);


}

