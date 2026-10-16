package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SalesTargetProgress;
import com.acme.backoffice.service.SalesTargetService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SalesTargetTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SalesTargetService service = new SalesTargetService(store);
    private final SalesRep anna = store.createSalesRep(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
    private final SalesRep carl = store.createSalesRep(new SalesRep(0, "Carl", "carl@example.com", Region.WEST));
    private final SalesRep dora = store.createSalesRep(new SalesRep(0, "Dora", "dora@example.com", Region.WEST));
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer zeta = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final YearMonth june = YearMonth.of(2024, 6);

    private void order(String number, Customer customer, LocalDate date, OrderStatus status, int quantity) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), quantity, new BigDecimal("10.00")))));
    }

    private void setUpOrders() {
        store.assignCustomerToSalesRep(alpha.id(), anna.id());
        store.assignCustomerToSalesRep(zeta.id(), carl.id());
        order("SO-1", alpha, LocalDate.of(2024, 6, 1), OrderStatus.CONFIRMED, 5);
        order("SO-2", alpha, LocalDate.of(2024, 6, 30), OrderStatus.DELIVERED, 3);
        order("SO-3", alpha, LocalDate.of(2024, 6, 15), OrderStatus.CANCELLED, 40);
        order("SO-4", alpha, LocalDate.of(2024, 7, 1), OrderStatus.CONFIRMED, 40);
        order("SO-5", alpha, LocalDate.of(2024, 5, 31), OrderStatus.CONFIRMED, 40);
        order("SO-6", zeta, LocalDate.of(2024, 6, 10), OrderStatus.CONFIRMED, 7);
    }

    @Test
    void reportsTargetRevenueAndPercentagePerRep() {
        setUpOrders();
        service.setTarget(anna.id(), june, new BigDecimal("100.00"));
        service.setTarget(carl.id(), june, new BigDecimal("90.00"));
        service.setTarget(dora.id(), june, new BigDecimal("50.00"));
        service.setTarget(anna.id(), YearMonth.of(2024, 7), new BigDecimal("1.00"));

        List<SalesTargetProgress> progress = service.progress(june);
        assertEquals(List.of("Anna", "Carl", "Dora"), progress.stream().map(SalesTargetProgress::salesRepName).toList());
        assertEquals(0, new BigDecimal("100.00").compareTo(progress.get(0).target()));
        assertEquals(0, new BigDecimal("80.00").compareTo(progress.get(0).revenue()));
        assertEquals(0, new BigDecimal("80.00").compareTo(progress.get(0).percentReached()));
        assertEquals(0, new BigDecimal("77.78").compareTo(progress.get(1).percentReached()));
        assertEquals(0, BigDecimal.ZERO.compareTo(progress.get(2).revenue()));
        assertEquals(0, BigDecimal.ZERO.compareTo(progress.get(2).percentReached()));
    }

    @Test
    void repsBelowEightyPercentExcludeExactlyEighty() {
        setUpOrders();
        service.setTarget(anna.id(), june, new BigDecimal("100.00"));
        service.setTarget(carl.id(), june, new BigDecimal("90.00"));
        service.setTarget(dora.id(), june, new BigDecimal("50.00"));
        assertEquals(List.of("Carl", "Dora"),
                service.repsBelowTarget(june).stream().map(SalesTargetProgress::salesRepName).toList());
    }

    @Test
    void settingTargetAgainReplacesItAndRepsWithoutTargetAreLeftOut() {
        setUpOrders();
        service.setTarget(anna.id(), june, new BigDecimal("100.00"));
        service.setTarget(anna.id(), june, new BigDecimal("400.00"));
        List<SalesTargetProgress> progress = service.progress(june);
        assertEquals(1, progress.size());
        assertEquals(0, new BigDecimal("20.00").compareTo(progress.get(0).percentReached()));
        assertEquals(0, service.progress(YearMonth.of(2024, 8)).size());
    }

    @Test
    void rejectsNonPositiveTarget() {
        assertThrows(IllegalArgumentException.class, () -> service.setTarget(anna.id(), june, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> service.setTarget(anna.id(), june, new BigDecimal("-1")));
    }
}
