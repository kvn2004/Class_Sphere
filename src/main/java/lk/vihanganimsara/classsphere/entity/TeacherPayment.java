package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Teacher_Payment")
public class TeacherPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tpId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    private LocalDate paymentMonth;
    private BigDecimal totalCollected;
    private BigDecimal teacherShare;

    @Lob
    private String calcDetails; // JSON stored as String

    private LocalDateTime generatedAt = LocalDateTime.now();
}
