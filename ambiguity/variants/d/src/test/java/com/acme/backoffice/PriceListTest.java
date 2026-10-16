package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerPrice;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.OrderEntryService;
import com.acme.backoffice.service.PriceListService;
import com.acme.backoffice.service.PromotionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PriceListTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final PriceListService prices = new PriceListService(store);
    private final Customer customer = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer other = store.createCustomer(new Customer(0, "Eta", "e@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
    private final LocalDate june1 = LocalDate.of(2024, 6, 1);
    private final LocalDate june30 = LocalDate.of(2024, 6, 30);

    @Test
    void specialPriceAppliesOnlyInsidePeriodForThatCustomer() {
        prices.addPrice(customer.id(), widget.id(), new BigDecimal("7.00"), june1, june30);
        assertEquals(new BigDecimal("7.00"), prices.priceFor(customer.id(), widget.id(), june1));
        assertEquals(new BigDecimal("7.00"), prices.priceFor(customer.id(), widget.id(), june30));
        assertEquals(new BigDecimal("10.00"), prices.priceFor(customer.id(), widget.id(), LocalDate.of(2024, 7, 1)));
        assertEquals(new BigDecimal("10.00"), prices.priceFor(other.id(), widget.id(), june1));
    }

    @Test
    void priceForShowsPromotionWhenNoSpecialPrice() {
        new PromotionService(store).createPromotion("HW20", new BigDecimal("20"), june1, june30, "Hardware");
        assertEquals(new BigDecimal("8.00"), prices.priceFor(customer.id(), widget.id(), june1));
    }

    @Test
    void rejectsOverlappingPeriodsButAllowsAdjacentOnes() {
        prices.addPrice(customer.id(), widget.id(), new BigDecimal("7.00"), june1, june30);
        assertThrows(IllegalStateException.class,
                () -> prices.addPrice(customer.id(), widget.id(), new BigDecimal("6.00"), june30, LocalDate.of(2024, 7, 15)));
        prices.addPrice(customer.id(), widget.id(), new BigDecimal("6.00"), LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 15));
        prices.addPrice(other.id(), widget.id(), new BigDecimal("6.00"), june1, june30);
        assertEquals(2, prices.listPrices(customer.id()).size());
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> prices.addPrice(customer.id(), widget.id(), new BigDecimal("-1"), june1, june30));
        assertThrows(IllegalArgumentException.class, () -> prices.addPrice(customer.id(), widget.id(), BigDecimal.ONE, june30, june1));
        assertThrows(IllegalArgumentException.class, () -> prices.addPrice(999, widget.id(), BigDecimal.ONE, june1, june30));
        assertThrows(IllegalArgumentException.class, () -> prices.addPrice(customer.id(), 999, BigDecimal.ONE, june1, june30));
    }

    @Test
    void removedPriceNoLongerApplies() {
        CustomerPrice price = prices.addPrice(customer.id(), widget.id(), new BigDecimal("7.00"), june1, june30);
        prices.removePrice(price.id());
        assertEquals(List.of(), prices.listPrices(customer.id()));
        assertEquals(new BigDecimal("10.00"), prices.priceFor(customer.id(), widget.id(), june1));
        assertThrows(IllegalArgumentException.class, () -> prices.removePrice(price.id()));
    }

    @Test
    void orderUsesSpecialPriceInsteadOfPromotion() {
        new PromotionService(store).createPromotion("HW50", new BigDecimal("50"), june1, june30, "Hardware");
        prices.addPrice(customer.id(), widget.id(), new BigDecimal("7.00"), june1, june30);
        OrderEntryService entry = new OrderEntryService(store);
        LocalDate date = LocalDate.of(2024, 6, 10);
        SalesOrder special = entry.placeOrder("SO-1", customer.id(), date, OrderStatus.DRAFT, List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00"))));
        assertEquals(new BigDecimal("7.00"), special.lines().get(0).unitPrice());
        assertNull(special.lines().get(0).promotionCode());
        SalesOrder regular = entry.placeOrder("SO-2", other.id(), date, OrderStatus.DRAFT, List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00"))));
        assertEquals(new BigDecimal("5.00"), regular.lines().get(0).unitPrice());
        assertEquals("HW50", regular.lines().get(0).promotionCode());
    }
}
