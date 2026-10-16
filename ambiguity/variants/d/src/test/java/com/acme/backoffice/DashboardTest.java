package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Dashboard;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.DashboardService;
import com.acme.backoffice.service.DiscountApprovalService;
import com.acme.backoffice.service.InvoiceService;
import com.acme.backoffice.service.OrderService;
import com.acme.backoffice.service.SupportTicketService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DashboardTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private final DashboardService dashboard = new DashboardService(store);
    private final Customer customer = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
    private final LocalDate day = LocalDate.of(2024, 1, 1);

    private void order(String number, LocalDate date, OrderStatus status, int quantity) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), quantity, new BigDecimal("10.00")))));
    }

    @Test
    void emptyDashboardIsAllZero() {
        Dashboard result = dashboard.summarize(day);
        assertEquals(0, result.openOrders());
        assertEquals(0, BigDecimal.ZERO.compareTo(result.openOrderValue()));
        assertEquals(0, BigDecimal.ZERO.compareTo(result.overdueInvoiceAmount()));
        assertEquals(0, BigDecimal.ZERO.compareTo(result.stockValue()));
        assertEquals(0, result.openTickets());
        assertEquals(0, result.backOrders());
        assertEquals(0, result.ordersAwaitingApproval());
    }

    @Test
    void dashboardSummarizesEveryFigureOnTheDate() {
        order("SO-1", day, OrderStatus.CONFIRMED, 3);
        order("SO-2", LocalDate.of(2024, 3, 1), OrderStatus.CONFIRMED, 1);
        order("SO-3", day, OrderStatus.DRAFT, 1);
        order("SO-4", day, OrderStatus.DRAFT, 1);
        order("SO-5", day, OrderStatus.DRAFT, 1);
        order("SO-6", day, OrderStatus.CONFIRMED, 2);
        DiscountApprovalService approvals = new DiscountApprovalService(store);
        approvals.setLineDiscount("SO-3", 1, new BigDecimal("20"));
        approvals.setLineDiscount("SO-4", 1, new BigDecimal("10"));
        approvals.setLineDiscount("SO-5", 1, new BigDecimal("25"));
        approvals.approve("SO-5", "Manager", day);
        store.createBackOrder(store.findSalesOrderByNumber("SO-1").orElseThrow().id(), day);

        orders.ship("SO-6", day);
        new InvoiceService(store).invoice("SO-6", day);

        SupportTicketService tickets = new SupportTicketService(store);
        tickets.create("T-1", customer.id(), null, "Open", TicketPriority.HIGH, day);
        tickets.create("T-2", customer.id(), null, "Closed", TicketPriority.LOW, day);
        tickets.changeStatus("T-2", TicketStatus.CLOSED, day.plusDays(30));

        Warehouse warehouse = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        Supplier supplier = store.createSupplier(new Supplier(0, "Nordic Supply", "n@example.com", "Norway", 4));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-1", supplier.id(), day, day.plusDays(10), PurchaseOrderStatus.RECEIVED,
                List.of(new PurchaseOrderLine(widget.id(), 10, new BigDecimal("2.50")))));
        store.setStockQuantity(warehouse.id(), widget.id(), 4);

        Dashboard early = dashboard.summarize(LocalDate.of(2024, 1, 15));
        assertEquals(1, early.openOrders());
        assertEquals(0, new BigDecimal("30.00").compareTo(early.openOrderValue()));
        assertEquals(0, BigDecimal.ZERO.compareTo(early.overdueInvoiceAmount()));
        assertEquals(0, new BigDecimal("10.00").compareTo(early.stockValue()));
        assertEquals(2, early.openTickets());
        assertEquals(1, early.backOrders());
        assertEquals(1, early.ordersAwaitingApproval());

        Dashboard late = dashboard.summarize(LocalDate.of(2024, 6, 1));
        assertEquals(2, late.openOrders());
        assertEquals(0, new BigDecimal("40.00").compareTo(late.openOrderValue()));
        assertEquals(0, new BigDecimal("20.00").compareTo(late.overdueInvoiceAmount()));
        assertEquals(1, late.openTickets());
    }

    @Test
    void dateIsRequired() {
        assertThrows(IllegalArgumentException.class, () -> dashboard.summarize(null));
    }
}
