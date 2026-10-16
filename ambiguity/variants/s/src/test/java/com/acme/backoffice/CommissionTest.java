package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RepCommission;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.service.CommissionService;
import com.acme.backoffice.service.SalesTargetService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommissionTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CommissionService service = new CommissionService(store);
    private final SalesTargetService targets = new SalesTargetService(store);
    private final SalesRep anna = store.createSalesRep(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
    private final SalesRep carl = store.createSalesRep(new SalesRep(0, "Carl", "carl@example.com", Region.WEST));
    private final SalesRep dora = store.createSalesRep(new SalesRep(0, "Dora", "dora@example.com", Region.WEST));
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer zeta = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final YearMonth june = YearMonth.of(2024, 6);
    private int counter;

    private void invoice(Customer customer, LocalDate date, InvoiceStatus status, String amount) {
        counter++;
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), date,
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        store.createInvoice(new Invoice(0, "INV-" + counter, order.id(), customer.id(), date, date.plusDays(30),
                new BigDecimal(amount), status));
    }

    private void setUpInvoices() {
        store.assignCustomerToSalesRep(alpha.id(), anna.id());
        store.assignCustomerToSalesRep(zeta.id(), carl.id());
        invoice(alpha, LocalDate.of(2024, 6, 1), InvoiceStatus.PAID, "600.00");
        invoice(alpha, LocalDate.of(2024, 6, 30), InvoiceStatus.PAID, "300.00");
        invoice(alpha, LocalDate.of(2024, 6, 10), InvoiceStatus.OPEN, "1000.00");
        invoice(alpha, LocalDate.of(2024, 6, 11), InvoiceStatus.CANCELLED, "1000.00");
        invoice(alpha, LocalDate.of(2024, 7, 1), InvoiceStatus.PAID, "1000.00");
        invoice(alpha, LocalDate.of(2024, 5, 31), InvoiceStatus.PAID, "1000.00");
        invoice(zeta, LocalDate.of(2024, 6, 5), InvoiceStatus.PAID, "250.50");
    }

    @Test
    void paysFivePercentOnlyOnThePartAboveTarget() {
        setUpInvoices();
        targets.setTarget(anna.id(), june, new BigDecimal("500.00"));
        RepCommission commission = service.commissionOf(anna.id(), june);
        assertEquals(0, new BigDecimal("900.00").compareTo(commission.paidAmount()));
        assertEquals(0, new BigDecimal("35.00").compareTo(commission.commission()));
    }

    @Test
    void paysThreePercentWithoutTargetOrAtTarget() {
        setUpInvoices();
        targets.setTarget(carl.id(), june, new BigDecimal("250.50"));
        assertEquals(0, new BigDecimal("27.00").compareTo(service.commissionOf(anna.id(), june).commission()));
        assertEquals(0, new BigDecimal("7.52").compareTo(service.commissionOf(carl.id(), june).commission()));
    }

    @Test
    void commissionOfAllRepsIncludesRepsWithoutInvoices() {
        setUpInvoices();
        targets.setTarget(anna.id(), june, new BigDecimal("800.00"));
        List<RepCommission> all = service.commissionOfAll(june);
        assertEquals(List.of("Anna", "Carl", "Dora"), all.stream().map(RepCommission::salesRepName).toList());
        assertEquals(0, new BigDecimal("29.00").compareTo(all.get(0).commission()));
        assertEquals(0, new BigDecimal("7.52").compareTo(all.get(1).commission()));
        assertEquals(0, BigDecimal.ZERO.compareTo(all.get(2).commission()));
    }

    @Test
    void rejectsUnknownRep() {
        assertThrows(IllegalArgumentException.class, () -> service.commissionOf(9999, june));
    }
}
