package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerActivity;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.CustomerActivityService;
import com.acme.backoffice.service.ReturnService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerActivityTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CustomerActivityService service = new CustomerActivityService(store);
    private final ReturnService returns = new ReturnService(store);
    private final Customer zeta = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("9.99"), "piece", true));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));

    private SalesOrder order(String number, Customer customer, LocalDate date, OrderStatus status, int quantity) {
        return store.createSalesOrder(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), quantity, new BigDecimal("10.00")),
                        new OrderLine(widget.id(), 1, new BigDecimal("0.50")))));
    }

    private void pay(String number, SalesOrder order, Customer customer, String amount) {
        Invoice invoice = store.createInvoice(new Invoice(0, number, order.id(), customer.id(), LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 4, 1), new BigDecimal("100.00"), InvoiceStatus.OPEN));
        store.createPayment(new Payment(0, invoice.id(), LocalDate.of(2024, 3, 5), new BigDecimal(amount)));
    }

    @Test
    void summarisesOrdersReturnsPaymentsAndCredits() {
        SalesOrder first = order("SO-1", zeta, LocalDate.of(2024, 3, 10), OrderStatus.DELIVERED, 2);
        SalesOrder last = order("SO-2", zeta, LocalDate.of(2024, 5, 20), OrderStatus.CONFIRMED, 1);
        order("SO-3", zeta, LocalDate.of(2024, 1, 1), OrderStatus.CANCELLED, 9);
        SalesOrder other = order("SO-4", alpha, LocalDate.of(2024, 4, 1), OrderStatus.DELIVERED, 5);
        pay("INV-1", first, zeta, "20.00");
        pay("INV-2", first, zeta, "5.50");
        pay("INV-3", other, alpha, "99.00");
        returns.createReturn("R-1", "SO-1", LocalDate.of(2024, 3, 15), "x", "WH-N", List.of(new ReturnLine(widget.id(), 1)));
        returns.createReturn("R-2", "SO-1", LocalDate.of(2024, 3, 16), "x", "WH-N", List.of(new ReturnLine(widget.id(), 2)));
        returns.createReturn("R-3", "SO-4", LocalDate.of(2024, 4, 5), "x", "WH-N", List.of(new ReturnLine(widget.id(), 1)));

        CustomerActivity activity = service.activity(zeta.id());
        assertEquals(LocalDate.of(2024, 3, 10), activity.firstOrderDate());
        assertEquals(LocalDate.of(2024, 5, 20), activity.lastOrderDate());
        assertEquals(2, activity.orderCount());
        assertEquals(2, activity.returnCount());
        assertEquals(0, new BigDecimal("31.00").compareTo(activity.totalOrdered()));
        assertEquals(0, new BigDecimal("25.50").compareTo(activity.totalPaid()));
        assertEquals(0, new BigDecimal("30.00").compareTo(activity.totalCredited()));
    }

    @Test
    void customerWithoutActivityHasNoDatesAndZeroTotals() {
        CustomerActivity activity = service.activity(alpha.id());
        assertNull(activity.firstOrderDate());
        assertNull(activity.lastOrderDate());
        assertEquals(0, activity.orderCount());
        assertEquals(0, activity.returnCount());
        assertEquals(0, BigDecimal.ZERO.compareTo(activity.totalOrdered()));
        assertEquals(0, BigDecimal.ZERO.compareTo(activity.totalPaid()));
        assertEquals(0, BigDecimal.ZERO.compareTo(activity.totalCredited()));
    }

    @Test
    void rejectsUnknownCustomer() {
        assertThrows(IllegalArgumentException.class, () -> service.activity(999));
    }
}
