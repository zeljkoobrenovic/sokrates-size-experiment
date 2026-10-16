package com.acme.backoffice;

import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Warehouse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WarehouseTest {
    private final DataStore store = new DataStore(Database.openInMemory());

    @Test
    void createAndFindByCode() {
        Warehouse created = store.warehouses().create(new Warehouse(0, "WH-1", "Main", "Oslo", Region.NORTH));
        assertTrue(created.id() > 0);
        assertEquals(created, store.warehouses().findByCode("WH-1").orElseThrow());
        assertTrue(store.warehouses().findByCode("WH-2").isEmpty());
    }

    @Test
    void listAllSortedByCode() {
        store.warehouses().create(new Warehouse(0, "WH-2", "Second", "Rome", Region.SOUTH));
        store.warehouses().create(new Warehouse(0, "WH-1", "First", "Oslo", Region.NORTH));
        assertEquals(List.of("WH-1", "WH-2"), store.warehouses().listByCode().stream().map(Warehouse::code).toList());
    }

    @Test
    void codeMustBeUnique() {
        store.warehouses().create(new Warehouse(0, "WH-1", "First", "Oslo", Region.NORTH));
        assertThrows(IllegalStateException.class,
                () -> store.warehouses().create(new Warehouse(0, "WH-1", "Other", "Rome", Region.SOUTH)));
    }
}
