package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String entityName;   // "User", "Order"
    private String entityId;       // e.g. 10
    private String action;        // "DELETE", "CREATE", "UPDATE"
    private String performedBy;  // e.g. "admin"
    private LocalDateTime timestamp;


    @Column(columnDefinition = "TEXT")
    private String extraData;    // JSON string for details
}

