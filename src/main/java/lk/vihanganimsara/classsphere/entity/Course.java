package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    @ManyToOne
    @JoinColumn(name = "subject_id")
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

    @OneToMany(mappedBy = "course")
    private List<CourseSession> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<CourseFee> fees = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Scholarship> scholarships = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<TeacherPayment> teacherPayments = new ArrayList<>();
}

