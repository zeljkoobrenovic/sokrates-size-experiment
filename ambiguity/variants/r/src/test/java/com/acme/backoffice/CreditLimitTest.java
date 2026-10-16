package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.CreditService;
import com.acme.backoffice.service.InvoiceService;
import com.acme.backoffice.service.OrderService;
import com.acme.backoffice.service.PaymentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreditLimitTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CreditService credit = new CreditService(store);
    private final OrderService orders = new OrderService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 1);
    private final Customer customer = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));

    private void draft(String number, int quantity) {
        store.orders().create(new SalesOrder(0, number, customer.id(), day, OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), quantity, new BigDecimal("10.00")))));
    }

    private BigDecimal money(String value) {
        return new BigDecimal(value);
    }

    @Test
    void noLimitMeansNoRestriction() {
        draft("SO-1", 1000);
        assertTrue(credit.availableCredit(customer.id()).isEmpty());
        assertEquals(OrderStatus.CONFIRMED, orders.confirm("SO-1", day).status());
    }

    @Test
    void availableCreditIsLimitMinusOpenInvoiceBalance() {
        credit.setCreditLimit(customer.id(), money("500.00"));
        assertEquals(0, money("500.00").compareTo(credit.availableCredit(customer.id()).orElseThrow()));
        draft("SO-1", 20);
        orders.confirm("SO-1", day);
        orders.ship("SO-1", day);
        new InvoiceService(store).invoice("SO-1", day);
        assertEquals(0, money("300.00").compareTo(credit.availableCredit(customer.id()).orElseThrow()));
        new PaymentService(store).pay("INV-0001", day, money("50.00"));
        assertEquals(0, money("350.00").compareTo(credit.availableCredit(customer.id()).orElseThrow()));
        new PaymentService(store).pay("INV-0001", day, money("150.00"));
        assertEquals(0, money("500.00").compareTo(credit.availableCredit(customer.id()).orElseThrow()));
    }

    @Test
    void confirmingBeyondLimitIsRefused() {
        credit.setCreditLimit(customer.id(), money("500.00"));
        draft("SO-1", 20);
        orders.confirm("SO-1", day);
        orders.ship("SO-1", day);
        new InvoiceService(store).invoice("SO-1", day);
        draft("SO-2", 31);
        assertThrows(IllegalStateException.class, () -> orders.confirm("SO-2", day));
        assertEquals(OrderStatus.DRAFT, store.orders().findByNumber("SO-2").orElseThrow().status());
        draft("SO-3", 30);
        assertEquals(OrderStatus.CONFIRMED, orders.confirm("SO-3", day).status());
    }

    @Test
    void removingLimitAndValidation() {
        credit.setCreditLimit(customer.id(), money("5.00"));
        credit.setCreditLimit(customer.id(), null);
        assertEquals(Optional.empty(), credit.availableCredit(customer.id()));
        assertThrows(IllegalArgumentException.class, () -> credit.setCreditLimit(customer.id(), money("-1")));
        assertThrows(IllegalArgumentException.class, () -> credit.setCreditLimit(999, money("1")));
    }
}
