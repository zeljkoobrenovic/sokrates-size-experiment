package com.acme.backoffice.model;

import java.time.LocalDate;

/** Who changed what: one audited change. The id is 0 until the entry has been created in the database. */
public record AuditEntry(long id, String user, LocalDate date, AuditEntityType entityType, long entityId,
                         String description) {
}
