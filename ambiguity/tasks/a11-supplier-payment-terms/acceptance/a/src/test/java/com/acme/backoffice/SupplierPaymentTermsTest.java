package com.acme.backoffice;

import com.acme.backoffice.model.Supplier;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Suppliers have payment terms (days), 30 unless agreed otherwise, kept when created and changed and shown in every
 * supplier listing. The new attribute is reached by reflection so that this test compiles against the code without it.
 */
class SupplierPaymentTermsTest {
    private final DataStore store = new DataStore(Database.openInMemory());

    private static final List<String> SUPPLIER_BEFORE = List.of("id", "name", "contactEmail", "country", "rating");

    private static Supplier supplier(long id, String name, String email, String country, int rating, int paymentTermsDays) {
        return AddedRecordComponent.create(Supplier.class, SUPPLIER_BEFORE, java.util.Map.of("id", id, "name", name,
                "contactEmail", email, "country", country, "rating", rating), paymentTermsDays, "A supplier's payment terms");
    }

    private static int paymentTermsDays(Supplier supplier) {
        return ((Number) AddedRecordComponent.read(supplier, SUPPLIER_BEFORE, "A supplier's payment terms")).intValue();
    }

    private static List<Integer> terms(List<Supplier> suppliers) {
        return suppliers.stream().map(SupplierPaymentTermsTest::paymentTermsDays).toList();
    }

    @Test
    void paymentTermsAreStoredAndShownInEverySupplierListing() {
        Supplier nordic = store.createSupplier(supplier(0, "Nordic", "n@example.com", "Sweden", 4, 45));
        Supplier acme = store.createSupplier(new Supplier(0, "Acme", "a@example.com", "USA", 5));
        store.createSupplier(supplier(0, "Baltic", "b@example.com", "Sweden", 2, 60));

        assertEquals(45, paymentTermsDays(nordic));
        assertEquals(30, paymentTermsDays(acme), "suppliers without agreed terms get 30 days");

        assertEquals(List.of(30, 60, 45), terms(store.listSuppliersByName()));
        assertEquals(List.of(60, 45), terms(store.listSuppliersByCountry("Sweden")));
        assertEquals(List.of(30, 45), terms(store.listSuppliersByMinimumRating(4)));
        assertEquals(45, paymentTermsDays(store.findSupplierById(nordic.id()).orElseThrow()));
    }

    @Test
    void changedPaymentTermsAreKept() {
        Supplier nordic = store.createSupplier(supplier(0, "Nordic", "n@example.com", "Sweden", 4, 45));
        store.updateSupplier(supplier(nordic.id(), nordic.name(), nordic.contactEmail(), nordic.country(), nordic.rating(), 14));

        assertEquals(14, paymentTermsDays(store.findSupplierById(nordic.id()).orElseThrow()));
        assertEquals(List.of(14), terms(store.listSuppliersByName()));
    }
}
