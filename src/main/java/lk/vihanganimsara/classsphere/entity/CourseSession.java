package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Course_Session")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sessionId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;

    private String notes;

    @OneToMany(mappedBy = "session")
    private List<Attendance> attendanceList = new ArrayList<>();
}

