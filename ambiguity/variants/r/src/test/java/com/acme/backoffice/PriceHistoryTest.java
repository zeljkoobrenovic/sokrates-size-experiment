package com.acme.backoffice;

import com.acme.backoffice.model.PriceChange;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.service.PriceHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceHistoryTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final PriceHistoryService prices = new PriceHistoryService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 10);
    private Product widget;
    private Product gadget;

    @BeforeEach
    void setUp() {
        widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        gadget = store.products().create(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("20.00"), "piece", true));
    }

    @Test
    void changeUpdatesCatalogueAndRecordsHistory() {
        PriceChange first = prices.changePrice(widget.id(), day, new BigDecimal("5.00"));
        assertEquals(0, new BigDecimal("4.50").compareTo(first.oldPrice()));
        prices.changePrice(widget.id(), day.plusDays(10), new BigDecimal("4.75"));

        assertEquals(0, new BigDecimal("4.75").compareTo(store.products().findById(widget.id()).orElseThrow().unitPrice()));
        List<PriceChange> history = prices.getHistory(widget.id());
        assertEquals(2, history.size());
        assertEquals(day, history.get(0).changeDate());
        assertEquals(0, new BigDecimal("5.00").compareTo(history.get(1).oldPrice()));
        assertEquals(0, new BigDecimal("4.75").compareTo(history.get(1).newPrice()));
        assertTrue(prices.getHistory(gadget.id()).isEmpty());
    }

    @Test
    void samePriceRecordsNothing() {
        assertNull(prices.changePrice(widget.id(), day, new BigDecimal("4.5")));
        assertTrue(prices.getHistory(widget.id()).isEmpty());
    }

    @Test
    void productsWithChangeInPeriod() {
        prices.changePrice(widget.id(), day, new BigDecimal("5.00"));
        prices.changePrice(widget.id(), day.plusDays(1), new BigDecimal("5.50"));
        prices.changePrice(gadget.id(), day.plusDays(20), new BigDecimal("21.00"));

        assertEquals(List.of("Widget"), prices.getProductsWithPriceChange(day, day.plusDays(5)).stream().map(Product::name).toList());
        assertEquals(List.of("Gadget", "Widget"), prices.getProductsWithPriceChange(day, day.plusDays(20)).stream().map(Product::name).toList());
        assertTrue(prices.getProductsWithPriceChange(day.plusDays(2), day.plusDays(19)).isEmpty());
    }

    @Test
    void invalidInputIsRejectedAndChangesNothing() {
        assertThrows(IllegalArgumentException.class, () -> prices.changePrice(widget.id(), null, BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> prices.changePrice(widget.id(), day, new BigDecimal("-1")));
        assertThrows(IllegalStateException.class, () -> prices.changePrice(9999, day, BigDecimal.ONE));
        assertEquals(0, new BigDecimal("4.50").compareTo(store.products().findById(widget.id()).orElseThrow().unitPrice()));
        assertTrue(prices.getProductsWithPriceChange(day, day).isEmpty());
    }
}
