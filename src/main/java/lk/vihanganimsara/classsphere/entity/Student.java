package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Students")
@Data
@Where(clause = "deleted = false")
public class Student {
    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "lk.vihanganimsara.classsphere.util.IdGenerator")
    private String id;
    private String name;
    private String email;
    private String telephone;
    private String address;
    private String guardianName;
    private String guardianTelephone;
    private String guardianEmail;

    @Column(unique = true, nullable = false)
    private String qrCodeContent;
    private String qrCodePath;
    private String photoPath;

    @OneToMany(mappedBy = "student" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<Attendance> attendanceRecords = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<Scholarship> scholarships = new ArrayList<>();

    // Soft delete flag
    private boolean deleted = false;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String deletedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
