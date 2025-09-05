package lk.vihanganimsara.classsphere.service.impl;

import lk.vihanganimsara.classsphere.entity.AuditLog;
import lk.vihanganimsara.classsphere.repository.AuditLogRepo;
import lk.vihanganimsara.classsphere.service.AuditLogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogsService {
    private final AuditLogRepo auditLogRepo;

    @Override
    public void logAction(String entityName, String entityId, String action,  String extraData) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "SYSTEM";

        AuditLog log = new AuditLog();
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setPerformedBy(currentUser);
        log.setTimestamp(LocalDateTime.now());
        log.setExtraData(extraData);
        auditLogRepo.save(log);
    }
}
