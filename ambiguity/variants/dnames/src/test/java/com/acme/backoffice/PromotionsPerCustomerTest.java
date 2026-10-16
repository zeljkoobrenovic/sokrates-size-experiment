package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerPromotions;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.service.OrderEntryService;
import com.acme.backoffice.service.PromotionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromotionsPerCustomerTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderEntryService entry = new OrderEntryService(store);
    private final PromotionService promotions = new PromotionService(store);
    private final Customer customer = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer other = store.createCustomer(new Customer(0, "Other", "o@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
    private final Product apples = store.createProduct(new Product(0, "A-1", "Apples", "Food", new BigDecimal("12.00"), "kg", true));
    private final LocalDate june1 = LocalDate.of(2024, 6, 1);
    private final LocalDate june30 = LocalDate.of(2024, 6, 30);

    private void place(String number, Customer who, LocalDate date, OrderStatus status, OrderLine... lines) {
        entry.placeOrder(number, who.id(), date, status, List.of(lines));
    }

    private OrderLine widgets(int quantity) {
        return new OrderLine(widget.id(), quantity, new BigDecimal("10.00"));
    }

    private OrderLine apples(int quantity) {
        return new OrderLine(apples.id(), quantity, new BigDecimal("12.00"));
    }

    @Test
    void listsPromotedLinesAndTotalDiscountOfTheCustomer() {
        promotions.createPromotion("HW20", new BigDecimal("20"), june1, june30, "Hardware");
        place("SO-2", customer, LocalDate.of(2024, 6, 20), OrderStatus.CONFIRMED, widgets(5));
        place("SO-1", customer, LocalDate.of(2024, 6, 5), OrderStatus.CONFIRMED, apples(2), widgets(10));
        place("SO-3", other, LocalDate.of(2024, 6, 6), OrderStatus.CONFIRMED, widgets(10));

        CustomerPromotions result = promotions.promotionsOfCustomer(customer.id(), june1, june30);
        assertEquals(2, result.lines().size());
        assertEquals("SO-1", result.lines().get(0).orderNumber());
        assertEquals(2, result.lines().get(0).lineNo());
        assertEquals("HW20", result.lines().get(0).promotionCode());
        assertEquals(0, new BigDecimal("20.00").compareTo(result.lines().get(0).discount()));
        assertEquals("SO-2", result.lines().get(1).orderNumber());
        assertEquals(0, new BigDecimal("30.00").compareTo(result.discount()));
    }

    @Test
    void ignoresCancelledOrdersAndOrdersOutsideThePeriod() {
        promotions.createPromotion("HW20", new BigDecimal("20"), june1, june30, "Hardware");
        place("SO-1", customer, LocalDate.of(2024, 6, 5), OrderStatus.CANCELLED, widgets(10));
        place("SO-2", customer, LocalDate.of(2024, 6, 20), OrderStatus.CONFIRMED, widgets(1));
        place("SO-3", customer, LocalDate.of(2024, 6, 21), OrderStatus.CONFIRMED, widgets(1));

        assertEquals(1, promotions.promotionsOfCustomer(customer.id(), june1, LocalDate.of(2024, 6, 20)).lines().size());
        CustomerPromotions none = promotions.promotionsOfCustomer(customer.id(), LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31));
        assertTrue(none.lines().isEmpty());
        assertEquals(0, BigDecimal.ZERO.compareTo(none.discount()));
    }

    @Test
    void hundredPercentPromotionUsesCataloguePriceAndPeriodIsValidated() {
        promotions.createPromotion("FREE", new BigDecimal("100"), june1, june30, "Hardware");
        place("SO-1", customer, june1, OrderStatus.CONFIRMED, widgets(3));
        assertEquals(0, new BigDecimal("30.00").compareTo(promotions.promotionsOfCustomer(customer.id(), june1, june30).discount()));
        assertThrows(IllegalArgumentException.class, () -> promotions.promotionsOfCustomer(customer.id(), june30, june1));
    }
}
