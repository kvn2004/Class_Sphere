package lk.vihanganimsara.classsphere.service;

public interface AuditLogsService {
    void logAction(String entityName, String entityId, String action,  String extraData);
}
