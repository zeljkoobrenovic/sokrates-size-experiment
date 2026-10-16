package com.acme.backoffice;

import com.acme.backoffice.model.CreditNote;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.ReturnService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreditNoteTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ReturnService returns = new ReturnService(store);
    private final LocalDate day = LocalDate.of(2024, 7, 10);
    private final Customer zeta = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("9.99"), "piece", true));
    private final Product gadget = store.createProduct(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("99.00"), "piece", true));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));

    private void order(String number, Customer customer) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), day, OrderStatus.DELIVERED,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal("4.50")), new OrderLine(gadget.id(), 2, new BigDecimal("30.00")))));
    }

    @Test
    void returnProducesCreditNoteAtOrderPrices() {
        order("SO-1", zeta);
        returns.createReturn("R-1", "SO-1", day.plusDays(2), "Damaged", "WH-N",
                List.of(new ReturnLine(widget.id(), 4), new ReturnLine(gadget.id(), 1)));
        List<CreditNote> notes = returns.listCreditNotes(zeta.id());
        assertEquals(1, notes.size());
        CreditNote note = notes.get(0);
        assertTrue(note.id() > 0);
        assertEquals("CN-R-1", note.number());
        assertEquals(zeta.id(), note.customerId());
        assertEquals(day.plusDays(2), note.creditDate());
        assertEquals(new BigDecimal("48.00"), note.amount());
    }

    @Test
    void listsAndTotalsPerCustomer() {
        order("SO-1", zeta);
        order("SO-2", alpha);
        returns.createReturn("R-2", "SO-1", day.plusDays(5), "x", "WH-N", List.of(new ReturnLine(widget.id(), 2)));
        returns.createReturn("R-1", "SO-1", day.plusDays(2), "x", "WH-N", List.of(new ReturnLine(gadget.id(), 1)));
        returns.createReturn("R-3", "SO-2", day.plusDays(3), "x", "WH-N", List.of(new ReturnLine(gadget.id(), 2)));
        assertEquals(List.of("CN-R-1", "CN-R-2"), returns.listCreditNotes(zeta.id()).stream().map(CreditNote::number).toList());
        assertEquals(new BigDecimal("39.00"), returns.totalCredited(zeta.id()));
        assertEquals(new BigDecimal("60.00"), returns.totalCredited(alpha.id()));
        assertEquals(0, returns.totalCredited(0).signum());
        assertTrue(returns.listCreditNotes(0).isEmpty());
    }

    @Test
    void rejectedReturnProducesNoCreditNote() {
        order("SO-1", zeta);
        assertThrows(IllegalArgumentException.class, () -> returns.createReturn("R-1", "SO-1", day, "x", "WH-N",
                List.of(new ReturnLine(widget.id(), 11))));
        assertTrue(returns.listCreditNotes(zeta.id()).isEmpty());
        assertEquals(0, returns.totalCredited(zeta.id()).signum());
    }
}
