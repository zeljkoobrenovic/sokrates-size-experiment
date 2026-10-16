package com.acme.backoffice;

import com.acme.backoffice.model.Supplier;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierTest {
    private final DataStore store = new DataStore(Database.openInMemory());

    private static List<String> names(List<Supplier> suppliers) {
        return suppliers.stream().map(Supplier::name).toList();
    }

    private void createData() {
        store.createSupplier(new Supplier(0, "Charlie", "c@example.com", "UK", 5));
        store.createSupplier(new Supplier(0, "Alpha", "a@example.com", "UK", 2));
        store.createSupplier(new Supplier(0, "Bravo", "b@example.com", "Peru", 4));
    }

    @Test
    void createAndUpdate() {
        Supplier created = store.createSupplier(new Supplier(0, "Alpha", "a@example.com", "UK", 2));
        assertTrue(created.id() > 0);
        assertEquals(List.of(created), store.findAllSuppliers());
        Supplier changed = new Supplier(created.id(), "Alpha Ltd", "x@example.com", "Peru", 5);
        store.updateSupplier(changed);
        assertEquals(List.of(changed), store.findAllSuppliers());
    }

    @Test
    void ratingMustBeBetweenOneAndFive() {
        assertThrows(IllegalStateException.class, () -> store.createSupplier(new Supplier(0, "A", "a@example.com", "UK", 0)));
        assertThrows(IllegalStateException.class, () -> store.createSupplier(new Supplier(0, "A", "a@example.com", "UK", 6)));
    }

    @Test
    void listAllSortedByName() {
        createData();
        assertEquals(List.of("Alpha", "Bravo", "Charlie"), names(store.findAllSuppliers()));
    }

    @Test
    void listByCountry() {
        createData();
        assertEquals(List.of("Alpha", "Charlie"), names(store.findSuppliersInCountry("UK")));
        assertTrue(store.findSuppliersInCountry("Nowhere").isEmpty());
    }

    @Test
    void listByMinimumRatingIsInclusive() {
        createData();
        assertEquals(List.of("Bravo", "Charlie"), names(store.findSuppliersWithRatingAtLeast(4)));
        assertEquals(List.of("Charlie"), names(store.findSuppliersWithRatingAtLeast(5)));
        assertEquals(3, store.findSuppliersWithRatingAtLeast(1).size());
    }
}
