package com.acme.backoffice;

import com.acme.backoffice.model.CreditNote;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ReasonCredit;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreditNotesPerPeriodTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ReturnService returns = new ReturnService(store);
    private final Product bolt;
    private int counter;

    CreditNotesPerPeriodTest() {
        store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        bolt = store.products().create(new Product(0, "B-1", "Bolt", "Fasteners", new BigDecimal("2.00"), "piece", true));
        Customer north = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer south = store.customers().create(new Customer(0, "Beta", "b@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        store.orders().create(new SalesOrder(0, "SO-1", north.id(), LocalDate.of(2024, 1, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(bolt.id(), 50, new BigDecimal("2.00")))));
        store.orders().create(new SalesOrder(0, "SO-2", south.id(), LocalDate.of(2024, 1, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(bolt.id(), 50, new BigDecimal("3.00")))));
    }

    private void giveBack(String order, LocalDate date, String reason, int quantity) {
        counter++;
        returns.createReturn("R-" + counter, order, date, reason, "WH-N", List.of(new ReturnLine(bolt.id(), quantity)));
    }

    private void fill() {
        giveBack("SO-1", LocalDate.of(2024, 3, 1), "Damaged", 3);
        giveBack("SO-2", LocalDate.of(2024, 3, 31), "Damaged", 2);
        giveBack("SO-1", LocalDate.of(2024, 3, 10), "Wrong item", 10);
        giveBack("SO-1", LocalDate.of(2024, 2, 29), "Damaged", 9);
        giveBack("SO-1", LocalDate.of(2024, 4, 1), "Late", 9);
    }

    @Test
    void listsCreditNotesBetweenTwoDatesInclusive() {
        fill();
        List<CreditNote> notes = returns.listCreditNotes(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of("CN-R-1", "CN-R-3", "CN-R-2"), notes.stream().map(CreditNote::number).toList());
        assertTrue(returns.listCreditNotes(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31)).isEmpty());
    }

    @Test
    void listsCreditNotesOfOneRegion() {
        fill();
        List<CreditNote> north = returns.listCreditNotes(Region.NORTH, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of("CN-R-1", "CN-R-3"), north.stream().map(CreditNote::number).toList());
        List<CreditNote> south = returns.listCreditNotes(Region.SOUTH, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of("CN-R-2"), south.stream().map(CreditNote::number).toList());
        assertTrue(returns.listCreditNotes(Region.EAST, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)).isEmpty());
    }

    @Test
    void reportsCreditedAmountPerReason() {
        fill();
        List<ReasonCredit> result = returns.creditedPerReason(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of("Wrong item", "Damaged"), result.stream().map(ReasonCredit::reason).toList());
        assertEquals(List.of(1, 2), result.stream().map(ReasonCredit::noteCount).toList());
        assertEquals(List.of(new BigDecimal("20.00"), new BigDecimal("12.00")),
                result.stream().map(ReasonCredit::creditedAmount).toList());
    }
}
