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
        store.suppliers().create(new Supplier(0, "Charlie", "c@example.com", "UK", 5));
        store.suppliers().create(new Supplier(0, "Alpha", "a@example.com", "UK", 2));
        store.suppliers().create(new Supplier(0, "Bravo", "b@example.com", "Peru", 4));
    }

    @Test
    void createAndUpdate() {
        Supplier created = store.suppliers().create(new Supplier(0, "Alpha", "a@example.com", "UK", 2));
        assertTrue(created.id() > 0);
        assertEquals(List.of(created), store.suppliers().list());
        Supplier changed = new Supplier(created.id(), "Alpha Ltd", "x@example.com", "Peru", 5);
        store.suppliers().update(changed);
        assertEquals(List.of(changed), store.suppliers().list());
    }

    @Test
    void ratingMustBeBetweenOneAndFive() {
        assertThrows(IllegalStateException.class, () -> store.suppliers().create(new Supplier(0, "A", "a@example.com", "UK", 0)));
        assertThrows(IllegalStateException.class, () -> store.suppliers().create(new Supplier(0, "A", "a@example.com", "UK", 6)));
    }

    @Test
    void listAllSortedByName() {
        createData();
        assertEquals(List.of("Alpha", "Bravo", "Charlie"), names(store.suppliers().list()));
    }

    @Test
    void listByCountry() {
        createData();
        assertEquals(List.of("Alpha", "Charlie"), names(store.suppliers().listInCountry("UK")));
        assertTrue(store.suppliers().listInCountry("Nowhere").isEmpty());
    }

    @Test
    void listByMinimumRatingIsInclusive() {
        createData();
        assertEquals(List.of("Bravo", "Charlie"), names(store.suppliers().listWithMinimumRating(4)));
        assertEquals(List.of("Charlie"), names(store.suppliers().listWithMinimumRating(5)));
        assertEquals(3, store.suppliers().listWithMinimumRating(1).size());
    }
}
