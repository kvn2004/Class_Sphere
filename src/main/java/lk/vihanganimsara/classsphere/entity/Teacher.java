package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "lk.vihanganimsara.classsphere.util.IdGenerator")
    private String id;
    private String name;
    private String phone;
    private String email;
    private String address;

    @Column(unique = true, nullable = false)
    private String qrCodeContent;
    private String qrCodePath;
    private String photoPath;

    @ManyToMany(mappedBy = "teachers")
    private List<Subject> subjects = new ArrayList<>();

    @OneToMany(mappedBy = "teacher")
    private List<Course> courses = new ArrayList<>();

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
