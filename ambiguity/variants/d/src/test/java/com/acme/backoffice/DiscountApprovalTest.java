package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderApproval;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.DiscountApprovalService;
import com.acme.backoffice.service.OrderService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DiscountApprovalTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final DiscountApprovalService service = new DiscountApprovalService(store);
    private final OrderService orders = new OrderService(store);
    private final LocalDate day = LocalDate.of(2024, 5, 1);

    DiscountApprovalTest() {
        Customer customer = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
        for (String number : List.of("SO-1", "SO-2", "SO-3")) {
            store.createSalesOrder(new SalesOrder(0, number, customer.id(), LocalDate.of(2024, 4, 1), OrderStatus.DRAFT,
                    List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        }
    }

    @Test
    void onlyDiscountsAboveFifteenPercentWaitForApproval() {
        service.setLineDiscount("SO-1", 1, new BigDecimal("15.00"));
        service.setLineDiscount("SO-2", 1, new BigDecimal("15.01"));
        assertEquals(List.of("SO-2"), service.listAwaitingApproval().stream().map(SalesOrder::number).toList());
    }

    @Test
    void unapprovedOrderCannotBeConfirmed() {
        service.setLineDiscount("SO-1", 1, new BigDecimal("20.00"));
        assertThrows(IllegalStateException.class, () -> orders.confirm("SO-1", day));
        service.approve("SO-1", "Maria", day);
        assertEquals(OrderStatus.CONFIRMED, orders.confirm("SO-1", day).status());
    }

    @Test
    void approvalRecordsApproverAndDateAndLeavesTheList() {
        service.setLineDiscount("SO-1", 1, new BigDecimal("20.00"));
        OrderApproval approval = service.approve("SO-1", "Maria", day);
        assertTrue(approval.approved());
        assertEquals("Maria", service.getApproval("SO-1").orElseThrow().approver());
        assertEquals(day, service.getApproval("SO-1").orElseThrow().date());
        assertTrue(service.listAwaitingApproval().isEmpty());
    }

    @Test
    void rejectedOrderCannotBeConfirmed() {
        service.setLineDiscount("SO-1", 1, new BigDecimal("20.00"));
        assertEquals(false, service.reject("SO-1", "Maria", day).approved());
        assertTrue(service.listAwaitingApproval().isEmpty());
        assertThrows(IllegalStateException.class, () -> orders.confirm("SO-1", day));
    }

    @Test
    void orderWithoutLargeDiscountConfirmsWithoutApproval() {
        service.setLineDiscount("SO-1", 1, new BigDecimal("10.00"));
        assertEquals(OrderStatus.CONFIRMED, orders.confirm("SO-1", day).status());
        assertEquals(OrderStatus.CONFIRMED, orders.confirm("SO-3", day).status());
    }

    @Test
    void changingTheDiscountAfterApprovalNeedsNewApproval() {
        service.setLineDiscount("SO-1", 1, new BigDecimal("20.00"));
        service.approve("SO-1", "Maria", day);
        service.setLineDiscount("SO-1", 1, new BigDecimal("40.00"));
        assertEquals(List.of("SO-1"), service.listAwaitingApproval().stream().map(SalesOrder::number).toList());
        assertThrows(IllegalStateException.class, () -> orders.confirm("SO-1", day));
    }

    @Test
    void invalidDecisionsAreRefused() {
        service.setLineDiscount("SO-1", 1, new BigDecimal("20.00"));
        assertThrows(IllegalStateException.class, () -> service.approve("SO-2", "Maria", day));
        assertThrows(IllegalArgumentException.class, () -> service.approve("SO-1", " ", day));
        assertThrows(IllegalArgumentException.class, () -> service.approve("SO-1", "Maria", null));
        assertThrows(IllegalArgumentException.class, () -> service.approve("SO-X", "Maria", day));
        service.approve("SO-1", "Maria", day);
        assertThrows(IllegalStateException.class, () -> service.reject("SO-1", "Maria", day));
        assertThrows(IllegalArgumentException.class, () -> service.setLineDiscount("SO-2", 2, BigDecimal.TEN));
        assertThrows(IllegalArgumentException.class, () -> service.setLineDiscount("SO-2", 1, new BigDecimal("101")));
    }
}
