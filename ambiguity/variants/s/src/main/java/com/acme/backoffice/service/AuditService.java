package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.AuditEntityType;
import com.acme.backoffice.model.AuditEntry;

import java.time.LocalDate;
import java.util.List;

/** The audit log of changes to customers, orders and invoices. */
public class AuditService {
    private final DataStore store;

    public AuditService(DataStore store) {
        this.store = store;
    }

    public AuditEntry record(String user, LocalDate date, AuditEntityType entityType, long entityId, String description) {
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("The user must not be empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("The date must not be empty");
        }
        if (entityType == null) {
            throw new IllegalArgumentException("The entity type must not be empty");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("The description must not be empty");
        }
        return store.createAuditEntry(new AuditEntry(0, user, date, entityType, entityId, description));
    }

    /** The audit entries of the entity, newest first. */
    public List<AuditEntry> listEntries(AuditEntityType entityType, long entityId) {
        return store.listAuditEntriesByEntity(entityType, entityId);
    }

    /** The audit entries of the user from the first date to the last date, both included, newest first. */
    public List<AuditEntry> listEntriesByUser(String user, LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("The end date must not be before the start date");
        }
        return store.listAuditEntriesByUser(user, from, to);
    }

    /** The audit entries of the entity type from the first date to the last date, both included, newest first. */
    public List<AuditEntry> listEntriesByEntityType(AuditEntityType entityType, LocalDate from, LocalDate to) {
        if (entityType == null) {
            throw new IllegalArgumentException("The entity type must not be empty");
        }
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("The end date must not be before the start date");
        }
        return store.listAuditEntriesByEntityType(entityType, from, to);
    }
}
