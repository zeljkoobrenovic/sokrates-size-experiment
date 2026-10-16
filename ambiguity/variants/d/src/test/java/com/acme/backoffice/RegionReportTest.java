package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionReport;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.ServiceLevel;
import com.acme.backoffice.model.Shipment;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.RegionReportService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegionReportTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final RegionReportService reports = new RegionReportService(store);
    private final Customer north = store.createCustomer(new Customer(0, "North", "n@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer north2 = store.createCustomer(new Customer(0, "North 2", "n2@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer south = store.createCustomer(new Customer(0, "South", "s@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse warehouse = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Carrier carrier = store.createCarrier(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50"), 2));
    private int counter;

    private SalesOrder order(Customer customer, LocalDate date, OrderStatus status, int quantity) {
        counter++;
        return store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), quantity, new BigDecimal("10.00")))));
    }

    private void invoice(SalesOrder order, Customer customer, LocalDate date, LocalDate due, String amount, InvoiceStatus status) {
        store.createInvoice(new Invoice(0, "INV-" + order.number(), order.id(), customer.id(), date, due, new BigDecimal(amount), status));
    }

    @Test
    void reportsFiguresOfTheRegionInThePeriod() {
        LocalDate from = LocalDate.of(2024, 6, 1);
        LocalDate to = LocalDate.of(2024, 6, 30);
        SalesOrder first = order(north, LocalDate.of(2024, 6, 2), OrderStatus.CONFIRMED, 2);
        SalesOrder second = order(north2, LocalDate.of(2024, 6, 30), OrderStatus.SHIPPED, 3);
        order(north, LocalDate.of(2024, 6, 10), OrderStatus.CANCELLED, 9);
        order(north, LocalDate.of(2024, 7, 1), OrderStatus.CONFIRMED, 9);
        SalesOrder other = order(south, LocalDate.of(2024, 6, 5), OrderStatus.CONFIRMED, 9);

        invoice(first, north, LocalDate.of(2024, 6, 3), LocalDate.of(2024, 6, 20), "20.00", InvoiceStatus.OPEN);
        invoice(second, north2, LocalDate.of(2024, 6, 30), LocalDate.of(2024, 7, 30), "30.00", InvoiceStatus.OPEN);
        invoice(other, south, LocalDate.of(2024, 6, 5), LocalDate.of(2024, 6, 6), "90.00", InvoiceStatus.OPEN);
        SalesOrder paid = order(north, LocalDate.of(2024, 5, 1), OrderStatus.DELIVERED, 1);
        invoice(paid, north, LocalDate.of(2024, 6, 4), LocalDate.of(2024, 6, 10), "10.00", InvoiceStatus.PAID);

        store.createShipment(new Shipment(0, first.id(), warehouse.id(), carrier.id(), "T-1", LocalDate.of(2024, 6, 4)));
        store.createShipment(new Shipment(0, second.id(), warehouse.id(), carrier.id(), "T-2", LocalDate.of(2024, 7, 2)));
        store.createShipment(new Shipment(0, other.id(), warehouse.id(), carrier.id(), "T-3", LocalDate.of(2024, 6, 6)));

        store.createSalesReturn(new SalesReturn(0, "R-1", first.id(), LocalDate.of(2024, 6, 15), "Damaged",
                List.of(new ReturnLine(widget.id(), 1))), warehouse.id());
        store.createSalesReturn(new SalesReturn(0, "R-2", second.id(), LocalDate.of(2024, 7, 15), "Damaged",
                List.of(new ReturnLine(widget.id(), 1))), warehouse.id());

        store.createSupportTicket(new SupportTicket(0, "T-1", north.id(), null, "Open", TicketPriority.NORMAL,
                TicketStatus.OPEN, LocalDate.of(2024, 6, 8), null));
        store.createSupportTicket(new SupportTicket(0, "T-2", north.id(), null, "Closed", TicketPriority.NORMAL,
                TicketStatus.CLOSED, LocalDate.of(2024, 6, 8), LocalDate.of(2024, 6, 9)));
        store.createSupportTicket(new SupportTicket(0, "T-3", south.id(), null, "Other", TicketPriority.NORMAL,
                TicketStatus.OPEN, LocalDate.of(2024, 6, 8), null));

        RegionReport report = reports.report(Region.NORTH, from, to);

        assertEquals(Region.NORTH, report.region());
        assertEquals(2, report.customerCount());
        assertEquals(2, report.orderCount());
        assertEquals(0, new BigDecimal("50.00").compareTo(report.revenue()));
        assertEquals(2, report.openInvoiceCount());
        assertEquals(0, new BigDecimal("20.00").compareTo(report.overdueAmount()));
        assertEquals(1, report.shipmentCount());
        assertEquals(1, report.returnCount());
        assertEquals(1, report.openTicketCount());
    }

    @Test
    void regionWithoutActivityHasZeroFigures() {
        RegionReport report = reports.report(Region.WEST, LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));

        assertEquals(new RegionReport(Region.WEST, LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30), 0, 0,
                report.revenue(), 0, report.overdueAmount(), 0, 0, 0), report);
        assertEquals(0, BigDecimal.ZERO.compareTo(report.revenue()));
        assertEquals(0, BigDecimal.ZERO.compareTo(report.overdueAmount()));
    }

    @Test
    void rejectsMissingArgumentsAndReversedPeriod() {
        LocalDate day = LocalDate.of(2024, 6, 1);
        assertThrows(IllegalArgumentException.class, () -> reports.report(null, day, day));
        assertThrows(IllegalArgumentException.class, () -> reports.report(Region.NORTH, day, LocalDate.of(2024, 5, 1)));
    }
}
