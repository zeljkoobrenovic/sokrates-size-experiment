package com.acme.backoffice;

import com.acme.backoffice.model.ChannelSales;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesChannel;
import com.acme.backoffice.model.SalesOrder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalesPerChannelTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer customer = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private void order(String number, LocalDate date, OrderStatus status, int quantity, SalesChannel channel) {
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), quantity, new BigDecimal("4.50")))));
        if (channel != null) {
            store.saveSalesOrderChannel(order.id(), channel);
        }
    }

    private void fill() {
        order("SO-1", LocalDate.of(2024, 3, 5), OrderStatus.CONFIRMED, 2, SalesChannel.WEB);
        order("SO-2", LocalDate.of(2024, 3, 1), OrderStatus.DELIVERED, 1, SalesChannel.WEB);
        order("SO-3", LocalDate.of(2024, 3, 10), OrderStatus.CANCELLED, 10, SalesChannel.WEB);
        order("SO-4", LocalDate.of(2024, 2, 29), OrderStatus.CONFIRMED, 1, SalesChannel.WEB);
        order("SO-5", LocalDate.of(2024, 3, 6), OrderStatus.CONFIRMED, 4, SalesChannel.PHONE);
        order("SO-6", LocalDate.of(2024, 3, 7), OrderStatus.CONFIRMED, 1, null);
    }

    private List<String> numbers(List<SalesOrder> orders) {
        return orders.stream().map(SalesOrder::number).toList();
    }

    @Test
    void listsOrdersOfChannelInclusiveAndSortedByDate() {
        fill();
        List<SalesOrder> orders = store.listSalesOrdersOfChannelPlacedBetween(SalesChannel.WEB, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 10));
        assertEquals(List.of("SO-2", "SO-1", "SO-3"), numbers(orders));
        assertEquals(1, orders.get(0).lines().size());
        assertEquals(List.of("SO-5"), numbers(store.listSalesOrdersOfChannelPlacedBetween(SalesChannel.PHONE, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31))));
        assertEquals(List.of(), store.listSalesOrdersOfChannelPlacedBetween(SalesChannel.EMAIL, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)));
    }

    @Test
    void reportsCountAndRevenuePerChannelWithoutCancelledOrders() {
        fill();
        List<ChannelSales> sales = store.sumNonCancelledSalesPerChannelBetween(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(2, sales.size());
        assertEquals(SalesChannel.PHONE, sales.get(0).channel());
        assertEquals(1, sales.get(0).orderCount());
        assertEquals(new BigDecimal("18.00"), sales.get(0).revenue());
        assertEquals(SalesChannel.WEB, sales.get(1).channel());
        assertEquals(2, sales.get(1).orderCount());
        assertEquals(new BigDecimal("13.50"), sales.get(1).revenue());
    }

    @Test
    void changingTheChannelMovesTheOrder() {
        fill();
        long id = store.findSalesOrderByNumber("SO-1").orElseThrow().id();
        store.saveSalesOrderChannel(id, SalesChannel.EMAIL);
        List<ChannelSales> sales = store.sumNonCancelledSalesPerChannelBetween(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(3, sales.size());
        assertEquals(List.of("SO-1"), numbers(store.listSalesOrdersOfChannelPlacedBetween(SalesChannel.EMAIL, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31))));
    }
}
