package com.acme.backoffice;

import com.acme.backoffice.model.AuditEntityType;
import com.acme.backoffice.model.AuditEntry;
import com.acme.backoffice.service.AuditService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuditLogTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final AuditService service = new AuditService(store);

    @Test
    void recordsEntryWithId() {
        AuditEntry entry = service.record("alice", LocalDate.of(2024, 6, 1), AuditEntityType.CUSTOMER, 7, "Changed email");
        assertTrue(entry.id() > 0);
        assertEquals("Changed email", entry.description());
    }

    @Test
    void listsEntriesOfEntityNewestFirst() {
        service.record("alice", LocalDate.of(2024, 6, 1), AuditEntityType.ORDER, 1, "first");
        service.record("bob", LocalDate.of(2024, 6, 3), AuditEntityType.ORDER, 1, "third");
        service.record("bob", LocalDate.of(2024, 6, 2), AuditEntityType.ORDER, 1, "second");
        service.record("bob", LocalDate.of(2024, 6, 4), AuditEntityType.INVOICE, 1, "other type");
        service.record("bob", LocalDate.of(2024, 6, 4), AuditEntityType.ORDER, 2, "other id");
        List<AuditEntry> entries = service.listEntries(AuditEntityType.ORDER, 1);
        assertEquals(List.of("third", "second", "first"), entries.stream().map(AuditEntry::description).toList());
        assertEquals(AuditEntityType.ORDER, entries.get(0).entityType());
    }

    @Test
    void listsEntriesOfUserBetweenDatesInclusive() {
        service.record("alice", LocalDate.of(2024, 5, 31), AuditEntityType.CUSTOMER, 1, "before");
        service.record("alice", LocalDate.of(2024, 6, 1), AuditEntityType.CUSTOMER, 1, "start");
        service.record("alice", LocalDate.of(2024, 6, 30), AuditEntityType.INVOICE, 2, "end");
        service.record("alice", LocalDate.of(2024, 7, 1), AuditEntityType.CUSTOMER, 1, "after");
        service.record("bob", LocalDate.of(2024, 6, 15), AuditEntityType.CUSTOMER, 1, "other user");
        List<AuditEntry> entries = service.listEntriesByUser("alice", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));
        assertEquals(List.of("end", "start"), entries.stream().map(AuditEntry::description).toList());
    }

    @Test
    void listsEntriesOfEntityTypeBetweenDatesInclusive() {
        service.record("alice", LocalDate.of(2024, 5, 31), AuditEntityType.INVOICE, 1, "before");
        service.record("alice", LocalDate.of(2024, 6, 1), AuditEntityType.INVOICE, 1, "start");
        service.record("bob", LocalDate.of(2024, 6, 30), AuditEntityType.INVOICE, 2, "end");
        service.record("bob", LocalDate.of(2024, 6, 30), AuditEntityType.INVOICE, 3, "end later id");
        service.record("alice", LocalDate.of(2024, 7, 1), AuditEntityType.INVOICE, 1, "after");
        service.record("bob", LocalDate.of(2024, 6, 15), AuditEntityType.ORDER, 1, "other type");
        List<AuditEntry> entries = service.listEntriesByEntityType(AuditEntityType.INVOICE, LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));
        assertEquals(List.of("end later id", "end", "start"), entries.stream().map(AuditEntry::description).toList());
    }

    @Test
    void rejectsInvalidEntityTypeRange() {
        LocalDate date = LocalDate.of(2024, 6, 1);
        assertThrows(IllegalArgumentException.class, () -> service.listEntriesByEntityType(null, date, date));
        assertThrows(IllegalArgumentException.class, () -> service.listEntriesByEntityType(AuditEntityType.ORDER, date, date.minusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> service.listEntriesByEntityType(AuditEntityType.ORDER, null, date));
    }

    @Test
    void rejectsInvalidInput() {
        LocalDate date = LocalDate.of(2024, 6, 1);
        assertThrows(IllegalArgumentException.class, () -> service.record(" ", date, AuditEntityType.ORDER, 1, "x"));
        assertThrows(IllegalArgumentException.class, () -> service.record("a", null, AuditEntityType.ORDER, 1, "x"));
        assertThrows(IllegalArgumentException.class, () -> service.record("a", date, null, 1, "x"));
        assertThrows(IllegalArgumentException.class, () -> service.record("a", date, AuditEntityType.ORDER, 1, ""));
        assertThrows(IllegalArgumentException.class, () -> service.listEntriesByUser("a", date, date.minusDays(1)));
    }
}
