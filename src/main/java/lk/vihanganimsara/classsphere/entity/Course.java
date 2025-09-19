package lk.vihanganimsara.classsphere.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Course")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Course {

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "lk.vihanganimsara.classsphere.util.IdGenerator")
    private String courseId;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    @JsonIgnore
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    private String title;

    @ManyToOne
    @JoinColumn(name = "default_hall_id")
    private Hall defaultHall;

    private LocalDate startMonth;
    private Boolean active = true;

    // CourseSessions depend on the Course → Cascade
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseSession> sessions = new ArrayList<>();

    // Enrollment should not be cascaded (student’s enrollment is independent)
    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments = new ArrayList<>();

    // CourseFees belong to Course → Cascade
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseFee> fees = new ArrayList<>();

    // Scholarships belong to Course → Cascade
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scholarship> scholarships = new ArrayList<>();

    // Payments should not be cascaded (they are financial records)
    @OneToMany(mappedBy = "course")
    private List<Payment> payments = new ArrayList<>();

    // TeacherPayments belong to Course → Cascade
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeacherPayment> teacherPayments = new ArrayList<>();
}
