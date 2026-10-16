package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.MonthlyPaymentTotal;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionPayments;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.PaymentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegionPaymentsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final PaymentService payments = new PaymentService(store);
    private final Customer north = store.customers().create(new Customer(0, "North", "n@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer south = store.customers().create(new Customer(0, "South", "s@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    private String invoice(Customer customer) {
        counter++;
        LocalDate date = LocalDate.of(2024, 1, 1);
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + counter, customer.id(), date,
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        store.invoices().create(new Invoice(0, "INV-" + counter, order.id(), customer.id(), date, date.plusDays(30),
                new BigDecimal("1000.00"), InvoiceStatus.OPEN));
        return "INV-" + counter;
    }

    @Test
    void listsPaymentsOfTheRegionInThePeriodWithTotalsPerMonth() {
        String n1 = invoice(north);
        String n2 = invoice(north);
        String s1 = invoice(south);
        payments.pay(n1, LocalDate.of(2024, 2, 20), new BigDecimal("100.00"));
        payments.pay(n2, LocalDate.of(2024, 2, 5), new BigDecimal("50.00"));
        payments.pay(n1, LocalDate.of(2024, 3, 1), new BigDecimal("25.00"));
        payments.pay(n1, LocalDate.of(2024, 1, 31), new BigDecimal("10.00"));
        payments.pay(n1, LocalDate.of(2024, 3, 2), new BigDecimal("5.00"));
        payments.pay(s1, LocalDate.of(2024, 2, 10), new BigDecimal("70.00"));

        RegionPayments result = payments.listByRegion(Region.NORTH, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 3, 1));

        assertEquals(Region.NORTH, result.region());
        assertEquals(List.of(LocalDate.of(2024, 2, 5), LocalDate.of(2024, 2, 20), LocalDate.of(2024, 3, 1)),
                result.payments().stream().map(p -> p.paymentDate()).toList());
        assertEquals(List.of(new MonthlyPaymentTotal(2024, 2, 2, new BigDecimal("150.00")),
                new MonthlyPaymentTotal(2024, 3, 1, new BigDecimal("25.00"))), result.monthlyTotals());
    }

    @Test
    void returnsNothingWhenThereAreNoPayments() {
        invoice(north);
        RegionPayments result = payments.listByRegion(Region.SOUTH, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 28));
        assertEquals(List.of(), result.payments());
        assertEquals(List.of(), result.monthlyTotals());
    }

    @Test
    void validatesArguments() {
        LocalDate day = LocalDate.of(2024, 2, 1);
        assertThrows(IllegalArgumentException.class, () -> payments.listByRegion(null, day, day));
        assertThrows(IllegalArgumentException.class, () -> payments.listByRegion(Region.NORTH, null, day));
        assertThrows(IllegalArgumentException.class, () -> payments.listByRegion(Region.NORTH, day, null));
        assertThrows(IllegalArgumentException.class, () -> payments.listByRegion(Region.NORTH, day, day.minusDays(1)));
    }
}
