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

class PriceListPerCategoryTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final PriceListService prices = new PriceListService(store);
    private final Customer customer = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer other = store.createCustomer(new Customer(0, "Eta", "e@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product hammer;
    private final Product bolt;
    private final Product milk;
    private final LocalDate june1 = LocalDate.of(2024, 6, 1);
    private final LocalDate june30 = LocalDate.of(2024, 6, 30);

    PriceListPerCategoryTest() {
        store.createCategory(new Category("HW", "Hardware", null));
        store.createCategory(new Category("FAS", "Fasteners", "HW"));
        store.createCategory(new Category("FD", "Food", null));
        hammer = store.createProduct(new Product(0, "H-1", "Hammer", "HW", new BigDecimal("10.00"), "piece", true));
        bolt = store.createProduct(new Product(0, "B-1", "Bolt", "FAS", new BigDecimal("1.00"), "piece", true));
        milk = store.createProduct(new Product(0, "M-1", "Milk", "FD", new BigDecimal("2.00"), "piece", true));
    }

    @Test
    void listsValidPricesOfCategoryAndSubcategories() {
        prices.addPrice(customer.id(), hammer.id(), new BigDecimal("7.00"), june1, june30);
        prices.addPrice(customer.id(), bolt.id(), new BigDecimal("0.80"), june1, june30);
        prices.addPrice(customer.id(), milk.id(), new BigDecimal("1.50"), june1, june30);
        List<CustomerPrice> hardware = prices.listPricesByCategory(customer.id(), "HW", june1);
        assertEquals(List.of(hammer.id(), bolt.id()), hardware.stream().map(CustomerPrice::productId).toList());
        assertEquals(List.of(bolt.id()), prices.listPricesByCategory(customer.id(), "FAS", june30).stream().map(CustomerPrice::productId).toList());
    }

    @Test
    void ignoresOtherCustomersAndPricesNotValidOnTheDate() {
        prices.addPrice(customer.id(), hammer.id(), new BigDecimal("7.00"), june1, june30);
        prices.addPrice(other.id(), hammer.id(), new BigDecimal("6.00"), june1, june30);
        assertEquals(1, prices.listPricesByCategory(customer.id(), "HW", june30).size());
        assertEquals(List.of(), prices.listPricesByCategory(customer.id(), "HW", LocalDate.of(2024, 5, 31)));
        assertEquals(List.of(), prices.listPricesByCategory(customer.id(), "HW", LocalDate.of(2024, 7, 1)));
        assertEquals(List.of(), prices.listPricesByCategory(customer.id(), "FD", june1));
    }

    @Test
    void rejectsMissingInput() {
        assertThrows(IllegalArgumentException.class, () -> prices.listPricesByCategory(customer.id(), " ", june1));
        assertThrows(IllegalArgumentException.class, () -> prices.listPricesByCategory(customer.id(), "HW", null));
    }
}
