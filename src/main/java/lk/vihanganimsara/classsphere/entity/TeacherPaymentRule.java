package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Teacher_PaymentRule")
public class TeacherPaymentRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ruleId;

    @Enumerated(EnumType.STRING)
    private HallType hallType;

    private BigDecimal percentage;
    private LocalDate effectiveDate;
}

