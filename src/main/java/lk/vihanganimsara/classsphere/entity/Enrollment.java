package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Enrollment")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Enrollment {
    @EmbeddedId
    private EnrollmentId id = new EnrollmentId();

    @ManyToOne
    @MapsId("studentId")
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    private Course course;

    private LocalDate enrolledOn = LocalDate.now();
    private Boolean active = true;
}



