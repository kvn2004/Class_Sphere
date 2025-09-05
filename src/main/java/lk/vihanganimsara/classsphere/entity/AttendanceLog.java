package lk.vihanganimsara.classsphere.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Attendance_Log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    private String method; // "QR" or "MANUAL"
    private String markedBy; // Teacher/Admin/Scanner device
    private LocalDateTime scannedAt = LocalDateTime.now();
}

