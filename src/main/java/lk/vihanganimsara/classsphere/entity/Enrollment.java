package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "enrollment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment {

    @EmbeddedId
    private EnrollmentId id = new EnrollmentId();

    @ManyToOne
    @MapsId("courseId")  // use courseId from EnrollmentId
    @JoinColumn(name = "course_id") // must match table column
    private Course course;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    private LocalDate enrolledOn = LocalDate.now();
    private Boolean active = true;
}
