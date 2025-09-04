package lk.vihanganimsara.classsphere.repository;

import lk.vihanganimsara.classsphere.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepo extends JpaRepository<AuditLog, Long> {
}
