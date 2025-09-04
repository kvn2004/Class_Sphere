package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Scholarship")
public class Scholarship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scholarshipId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private BigDecimal discountPercentage;
    private LocalDate effectiveDate;
}

