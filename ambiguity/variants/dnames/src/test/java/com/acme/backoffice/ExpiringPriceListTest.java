package com.acme.backoffice;

import com.acme.backoffice.model.Category;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerPrice;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.service.PriceListService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpiringPriceListTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final PriceListService prices = new PriceListService(store);
    private final Customer first = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer second = store.createCustomer(new Customer(0, "Eta", "e@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product hammer;
    private final LocalDate june1 = LocalDate.of(2024, 6, 1);

    ExpiringPriceListTest() {
        store.createCategory(new Category("HW", "Hardware", null));
        hammer = store.createProduct(new Product(0, "H-1", "Hammer", "HW", new BigDecimal("10.00"), "piece", true));
    }

    @Test
    void listsPricesEndingWithinTheWindowPerCustomer() {
        prices.addPrice(second.id(), hammer.id(), new BigDecimal("6.00"), june1, LocalDate.of(2024, 6, 5));
        prices.addPrice(first.id(), hammer.id(), new BigDecimal("7.00"), june1, LocalDate.of(2024, 6, 10));
        prices.addPrice(first.id(), hammer.id(), new BigDecimal("8.00"), LocalDate.of(2024, 6, 11), LocalDate.of(2024, 6, 20));
        List<CustomerPrice> expiring = prices.listExpiringPrices(june1, 10);
        assertEquals(List.of(first.id(), second.id()), expiring.stream().map(CustomerPrice::customerId).toList());
        assertEquals(List.of(new BigDecimal("7.00"), new BigDecimal("6.00")), expiring.stream().map(CustomerPrice::unitPrice).toList());
    }

    @Test
    void windowStartsAfterTheDateAndEndsInclusive() {
        prices.addPrice(first.id(), hammer.id(), new BigDecimal("7.00"), LocalDate.of(2024, 5, 1), june1);
        prices.addPrice(first.id(), hammer.id(), new BigDecimal("8.00"), LocalDate.of(2024, 6, 2), LocalDate.of(2024, 6, 11));
        List<CustomerPrice> expiring = prices.listExpiringPrices(june1, 10);
        assertEquals(List.of(new BigDecimal("8.00")), expiring.stream().map(CustomerPrice::unitPrice).toList());
        assertEquals(List.of(), prices.listExpiringPrices(june1, 9));
    }

    @Test
    void rejectsMissingInput() {
        assertThrows(IllegalArgumentException.class, () -> prices.listExpiringPrices(null, 10));
        assertThrows(IllegalArgumentException.class, () -> prices.listExpiringPrices(june1, 0));
    }
}
