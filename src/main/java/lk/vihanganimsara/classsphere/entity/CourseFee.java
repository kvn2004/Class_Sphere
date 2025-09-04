package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Course_Fee")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer feeId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private BigDecimal monthlyFee;
    private LocalDate effectiveDate;
}

