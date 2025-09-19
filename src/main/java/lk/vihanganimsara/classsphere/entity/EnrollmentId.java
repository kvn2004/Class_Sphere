package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentId implements Serializable {
    @Column(name = "student_id")
    private String studentId;

    @Column(name = "course_id")
    private String courseId;
}
