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

/** In the year-over-year comparison, a month without orders has no average order value to compare. */
class AverageOrderValueChangeTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer west = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer north = store.createCustomer(new Customer(0, "Nord", "n@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.00"), "piece", true));

    private void order(String number, Customer customer, LocalDate date, int quantity) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), date, OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), quantity, new BigDecimal("4.00")))));
    }

    @Test
    void regionWithoutOrdersThisMonthHasNoAverageOrderValueChange() {
        order("SO-1", west, LocalDate.of(2024, 3, 5), 10);
        order("SO-2", west, LocalDate.of(2023, 3, 5), 5);
        order("SO-3", north, LocalDate.of(2023, 3, 20), 2);

        List<YearOverYearSales> regions = store.findYearOverYearSalesPerRegion(YearMonth.of(2024, 3));
        YearOverYearSales northRegion = regions.get(0);
        assertEquals(Region.NORTH, northRegion.region());
        assertEquals(0, northRegion.orderCount());
        assertEquals(new BigDecimal("-100.00"), northRegion.revenueChangePercent());
        assertNull(northRegion.averageOrderValueChangePercent(), "no orders this month: no average to compare");

        YearOverYearSales westRegion = regions.get(1);
        assertEquals(new BigDecimal("100.00"), westRegion.averageOrderValueChangePercent());
    }
}
