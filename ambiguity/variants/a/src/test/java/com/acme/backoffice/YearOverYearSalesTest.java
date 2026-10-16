package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.YearOverYearSales;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class YearOverYearSalesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer west = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer north = store.createCustomer(new Customer(0, "Nord", "n@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.00"), "piece", true));

    private void order(String number, Customer customer, LocalDate date, OrderStatus status, int quantity) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), quantity, new BigDecimal("4.00")))));
    }

    @Test
    void comparesMonthWithSameMonthOfPreviousYear() {
        order("SO-1", west, LocalDate.of(2024, 3, 1), OrderStatus.CONFIRMED, 5);
        order("SO-2", west, LocalDate.of(2024, 3, 31), OrderStatus.CONFIRMED, 10);
        order("SO-3", west, LocalDate.of(2024, 3, 15), OrderStatus.CANCELLED, 100);
        order("SO-4", west, LocalDate.of(2024, 4, 1), OrderStatus.CONFIRMED, 100);
        order("SO-5", west, LocalDate.of(2023, 3, 10), OrderStatus.CONFIRMED, 5);
        order("SO-6", west, LocalDate.of(2023, 4, 10), OrderStatus.CONFIRMED, 100);
        order("SO-7", north, LocalDate.of(2023, 3, 20), OrderStatus.DELIVERED, 2);
        order("SO-8", north, LocalDate.of(2022, 3, 20), OrderStatus.DELIVERED, 100);

        YearOverYearSales all = store.findYearOverYearSales(YearMonth.of(2024, 3));
        assertNull(all.region());
        assertEquals(2, all.orderCount());
        assertEquals(new BigDecimal("60.00"), all.revenue());
        assertEquals(2, all.previousOrderCount());
        assertEquals(new BigDecimal("28.00"), all.previousRevenue());
        assertEquals(new BigDecimal("30.00"), all.averageOrderValue());
        assertEquals(new BigDecimal("14.00"), all.previousAverageOrderValue());
        assertEquals(new BigDecimal("114.29"), all.revenueChangePercent());
        assertEquals(new BigDecimal("0.00"), all.orderChangePercent());
        assertEquals(new BigDecimal("114.29"), all.averageOrderValueChangePercent());

        List<YearOverYearSales> regions = store.findYearOverYearSalesPerRegion(YearMonth.of(2024, 3));
        assertEquals(2, regions.size());
        assertEquals(Region.NORTH, regions.get(0).region());
        assertEquals(0, regions.get(0).orderCount());
        assertEquals(1, regions.get(0).previousOrderCount());
        assertEquals(new BigDecimal("8.00"), regions.get(0).previousRevenue());
        assertEquals(new BigDecimal("-100.00"), regions.get(0).revenueChangePercent());
        assertEquals(Region.WEST, regions.get(1).region());
        assertEquals(2, regions.get(1).orderCount());
        assertEquals(new BigDecimal("60.00"), regions.get(1).revenue());
        assertEquals(1, regions.get(1).previousOrderCount());
        assertEquals(new BigDecimal("20.00"), regions.get(1).previousRevenue());
        assertEquals(new BigDecimal("200.00"), regions.get(1).revenueChangePercent());
    }

    @Test
    void handlesMonthsWithoutOrders() {
        YearOverYearSales all = store.findYearOverYearSales(YearMonth.of(2024, 2));
        assertEquals(0, all.orderCount());
        assertEquals(0, BigDecimal.ZERO.compareTo(all.revenue()));
        assertEquals(0, BigDecimal.ZERO.compareTo(all.averageOrderValue()));
        assertNull(all.revenueChangePercent());
        assertEquals(0, store.findYearOverYearSalesPerRegion(YearMonth.of(2024, 2)).size());
    }

    @Test
    void leapDayComparesWithEndOfFebruary() {
        order("SO-1", west, LocalDate.of(2024, 2, 29), OrderStatus.CONFIRMED, 1);
        order("SO-2", west, LocalDate.of(2023, 2, 28), OrderStatus.CONFIRMED, 2);
        YearOverYearSales all = store.findYearOverYearSales(YearMonth.of(2024, 2));
        assertEquals(1, all.orderCount());
        assertEquals(1, all.previousOrderCount());
        assertEquals(new BigDecimal("8.00"), all.previousRevenue());
    }
}
