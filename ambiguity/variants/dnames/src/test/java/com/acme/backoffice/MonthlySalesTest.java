package com.acme.backoffice;

import com.acme.backoffice.model.CreditNote;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.MonthlySales;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.Warehouse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonthlySalesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer customer = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse warehouse = store.createWarehouse(new Warehouse(0, "WH-1", "Main", "Austin", Region.WEST));

    private SalesOrder order(String number, LocalDate date, OrderStatus status, int quantity) {
        return store.createSalesOrder(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), quantity, new BigDecimal("4.50")))));
    }

    @Test
    void reportsAllTwelveMonthsOfTheYear() {
        List<MonthlySales> months = store.findMonthlySalesOfYear(2024);
        assertEquals(12, months.size());
        assertEquals(1, months.get(0).month());
        assertEquals(12, months.get(11).month());
        assertEquals(0, months.get(5).orderCount());
        assertEquals(0, new BigDecimal("0").compareTo(months.get(5).revenue()));
        assertEquals(0, new BigDecimal("0").compareTo(months.get(5).creditedAmount()));
    }

    @Test
    void countsOrdersCustomersReturnsAndCreditsPerMonth() {
        store.updateCustomerCreatedDate(customer.id(), LocalDate.of(2024, 3, 31));
        Customer other = store.createCustomer(new Customer(0, "Other", "o@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
        store.updateCustomerCreatedDate(other.id(), LocalDate.of(2023, 3, 1));
        store.createCustomer(new Customer(0, "Undated", "u@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));

        order("SO-1", LocalDate.of(2024, 3, 5), OrderStatus.CONFIRMED, 2);
        SalesOrder delivered = order("SO-2", LocalDate.of(2024, 3, 31), OrderStatus.DELIVERED, 1);
        order("SO-3", LocalDate.of(2024, 3, 10), OrderStatus.CANCELLED, 10);
        order("SO-4", LocalDate.of(2024, 4, 1), OrderStatus.CONFIRMED, 4);
        order("SO-5", LocalDate.of(2023, 12, 31), OrderStatus.CONFIRMED, 1);
        order("SO-6", LocalDate.of(2025, 1, 1), OrderStatus.CONFIRMED, 1);

        SalesReturn salesReturn = store.createSalesReturn(new SalesReturn(0, "RET-1", delivered.id(), LocalDate.of(2024, 4, 2), "Damaged",
                List.of(new ReturnLine(widget.id(), 1))), warehouse.id());
        store.createCreditNote(new CreditNote(0, "CN-1", customer.id(), salesReturn.id(), LocalDate.of(2024, 4, 3), new BigDecimal("4.50")));

        List<MonthlySales> months = store.findMonthlySalesOfYear(2024);
        MonthlySales march = months.get(2);
        assertEquals(2, march.orderCount());
        assertEquals(new BigDecimal("13.50"), march.revenue());
        assertEquals(1, march.newCustomers());
        assertEquals(0, march.returnCount());
        MonthlySales april = months.get(3);
        assertEquals(1, april.orderCount());
        assertEquals(new BigDecimal("18.00"), april.revenue());
        assertEquals(0, april.newCustomers());
        assertEquals(1, april.returnCount());
        assertEquals(new BigDecimal("4.50"), april.creditedAmount());
        assertEquals(0, months.get(0).orderCount());
        assertEquals(0, months.get(11).orderCount());
    }
}
