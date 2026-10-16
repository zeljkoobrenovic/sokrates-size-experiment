package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerOverLimit;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionOverLimit;
import com.acme.backoffice.model.RepOverLimit;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.service.CreditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomersOverCreditLimitTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CreditService credit = new CreditService(store);
    private Product bolt;
    private int counter;

    private Customer customer(String name, Region region, SalesRep rep, String limit) {
        Customer c = store.customers().create(new Customer(0, name, name + "@example.com", null, "X", region, CustomerStatus.ACTIVE));
        if (rep != null) {
            store.customers().assignToSalesRep(c.id(), rep.id());
        }
        if (limit != null) {
            credit.setCreditLimit(c.id(), new BigDecimal(limit));
        }
        return c;
    }

    private Invoice invoice(Customer customer, String amount, InvoiceStatus status) {
        counter++;
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + counter, customer.id(), LocalDate.of(2024, 1, 1),
                OrderStatus.DELIVERED, List.of(new OrderLine(bolt.id(), 1, new BigDecimal("2.00")))));
        return store.invoices().create(new Invoice(0, "INV-" + counter, order.id(), customer.id(), LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 3, 31), new BigDecimal(amount), status));
    }

    private String describe(CustomerOverLimit c) {
        return c.region() + ":" + c.salesRepName() + ":" + c.name() + ":" + c.openAmount().toPlainString() + "/"
                + c.creditLimit().toPlainString();
    }

    private String describe(RegionOverLimit r) {
        return r.region() + ":" + r.customerCount() + ":" + r.excessAmount().toPlainString();
    }

    private String describe(RepOverLimit r) {
        return r.name() + ":" + r.customerCount() + ":" + r.excessAmount().toPlainString();
    }

    @BeforeEach
    void setUp() {
        bolt = store.products().create(new Product(0, "B-1", "Bolt", "Fasteners", new BigDecimal("2.00"), "piece", true));
        SalesRep rita = store.salesReps().create(new SalesRep(0, "Rita", "rita@example.com", Region.NORTH));
        SalesRep sam = store.salesReps().create(new SalesRep(0, "Sam", "sam@example.com", Region.NORTH));
        Customer anna = customer("Anna", Region.NORTH, rita, "100.00");
        Customer ben = customer("Ben", Region.NORTH, sam, "100.00");
        Customer cleo = customer("Cleo", Region.NORTH, rita, "100.00");
        Customer dan = customer("Dan", Region.SOUTH, null, "10.00");
        Customer eve = customer("Eve", Region.SOUTH, rita, null);
        Customer fay = customer("Fay", Region.SOUTH, sam, "100.00");
        invoice(anna, "150.00", InvoiceStatus.OPEN);       // 50 over
        invoice(ben, "130.00", InvoiceStatus.OPEN);        // 30 over
        invoice(cleo, "100.00", InvoiceStatus.OPEN);       // exactly at the limit: not over
        invoice(dan, "40.00", InvoiceStatus.OPEN);         // 30 over, no rep
        invoice(eve, "999.00", InvoiceStatus.OPEN);        // no limit
        invoice(fay, "500.00", InvoiceStatus.PAID);        // not open
        invoice(fay, "500.00", InvoiceStatus.CANCELLED);   // not open
    }

    @Test
    void listsCustomersOverTheirLimitPerRegionAndRep() {
        assertEquals(List.of("NORTH:Rita:Anna:150.00/100.00", "NORTH:Sam:Ben:130.00/100.00", "SOUTH:null:Dan:40.00/10.00"),
                credit.getCustomersOverLimit(LocalDate.of(2024, 4, 1)).stream().map(this::describe).toList());
    }

    @Test
    void summarisesPerRegionAndPerSalesRep() {
        LocalDate day = LocalDate.of(2024, 4, 1);
        assertEquals(List.of("NORTH:2:80.00", "SOUTH:1:30.00"),
                credit.getOverLimitPerRegion(day).stream().map(this::describe).toList());
        assertEquals(List.of("Rita:1:50.00", "Sam:1:30.00"),
                credit.getOverLimitPerSalesRep(day).stream().map(this::describe).toList());
    }

    @Test
    void paymentsReduceTheOpenAmountOnlyFromTheirDate() {
        Customer gus = customer("Gus", Region.WEST, null, "50.00");
        Invoice invoice = invoice(gus, "120.00", InvoiceStatus.OPEN);
        store.payments().create(new Payment(0, invoice.id(), LocalDate.of(2024, 3, 20), new BigDecimal("80.00")));
        assertTrue(credit.getCustomersOverLimit(LocalDate.of(2024, 3, 19)).stream().anyMatch(c -> c.name().equals("Gus")));
        assertTrue(credit.getCustomersOverLimit(LocalDate.of(2024, 3, 25)).stream().noneMatch(c -> c.name().equals("Gus")));
        assertTrue(credit.getCustomersOverLimit(LocalDate.of(2024, 3, 20)).stream().noneMatch(c -> c.name().equals("Gus")));
    }

    @Test
    void ignoresInvoicesDatedAfterTheDate() {
        assertTrue(credit.getCustomersOverLimit(LocalDate.of(2024, 2, 28)).isEmpty());
        assertTrue(credit.getOverLimitPerRegion(LocalDate.of(2024, 2, 28)).isEmpty());
        assertTrue(credit.getOverLimitPerSalesRep(LocalDate.of(2024, 2, 28)).isEmpty());
    }

    @Test
    void requiresADate() {
        assertThrows(IllegalArgumentException.class, () -> credit.getCustomersOverLimit(null));
        assertThrows(IllegalArgumentException.class, () -> credit.getOverLimitPerRegion(null));
        assertThrows(IllegalArgumentException.class, () -> credit.getOverLimitPerSalesRep(null));
    }
}
