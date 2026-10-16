package com.acme.backoffice;

import com.acme.backoffice.model.AuditEntityType;
import com.acme.backoffice.model.AuditEntry;

import java.time.LocalDate;
import java.util.List;

/**
 * The audit trail (audit log): who changed which entity when, recorded as entries and listed per entity, per user or
 * per entity type, always newest first.
 */
public final class AuditStore {
    private final Jdbc jdbc;

    AuditStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public AuditEntry createEntry(AuditEntry entry) {
        long id = jdbc.insert("Cannot create audit entry",
                "INSERT INTO audit_log (user_name, change_date, entity_type, entity_id, description) VALUES (?, ?, ?, ?, ?)",
                entry.user(), entry.date(), entry.entityType().name(), entry.entityId(), entry.description());
        return new AuditEntry(id, entry.user(), entry.date(), entry.entityType(), entry.entityId(), entry.description());
    }

    /** The audit entries of one entity, newest first, then by highest id. */
    public List<AuditEntry> listOfEntity(AuditEntityType entityType, long entityId) {
        return list("Cannot list audit entries by entity", "entity_type = ? AND entity_id = ?", entityType.name(), entityId);
    }

    /** The audit entries of the user dated between the two dates, inclusive, newest first, then by highest id. */
    public List<AuditEntry> listOfUser(String user, LocalDate from, LocalDate to) {
        return list("Cannot list audit entries by user", "user_name = ? AND change_date >= ? AND change_date <= ?",
                user, from, to);
    }

    /** The audit entries of all entities of the type dated between the two dates, inclusive, newest first, then by highest id. */
    public List<AuditEntry> listOfEntityType(AuditEntityType entityType, LocalDate from, LocalDate to) {
        return list("Cannot list audit entries by entity type", "entity_type = ? AND change_date >= ? AND change_date <= ?",
                entityType.name(), from, to);
    }

    private List<AuditEntry> list(String error, String condition, Object... params) {
        return jdbc.list(error, "SELECT id, user_name, change_date, entity_type, entity_id, description FROM audit_log "
                        + "WHERE " + condition + " ORDER BY change_date DESC, id DESC",
                rs -> new AuditEntry(rs.getLong("id"), rs.getString("user_name"), rs.getObject("change_date", LocalDate.class),
                        AuditEntityType.valueOf(rs.getString("entity_type")), rs.getLong("entity_id"),
                        rs.getString("description")),
                params);
    }
}
