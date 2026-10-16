package com.acme.backoffice;

import com.acme.backoffice.model.CountryOrderCount;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.CountryOrdersService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountryOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CountryOrdersService service = new CountryOrdersService(store);
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    private Customer customer(String country) {
        counter++;
        return store.customers().create(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, country, Region.NORTH, CustomerStatus.ACTIVE));
    }

    private void order(String number, Customer customer, LocalDate date, OrderStatus status) {
        store.orders().create(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
    }

    private void fill() {
        Customer uk1 = customer("UK");
        Customer uk2 = customer("UK");
        Customer de = customer("DE");
        Customer fr = customer("FR");
        order("A", uk1, LocalDate.of(2024, 1, 20), OrderStatus.CONFIRMED);
        order("B", uk2, LocalDate.of(2024, 1, 1), OrderStatus.CONFIRMED);
        order("C", uk1, LocalDate.of(2024, 1, 31), OrderStatus.CONFIRMED);
        order("D", uk1, LocalDate.of(2023, 12, 31), OrderStatus.CONFIRMED);
        order("E", uk1, LocalDate.of(2024, 2, 1), OrderStatus.CONFIRMED);
        order("F", uk1, LocalDate.of(2024, 1, 15), OrderStatus.CANCELLED);
        order("G", de, LocalDate.of(2024, 1, 10), OrderStatus.CONFIRMED);
        order("H", fr, LocalDate.of(2024, 1, 11), OrderStatus.CONFIRMED);
        order("I", fr, LocalDate.of(2024, 1, 12), OrderStatus.CONFIRMED);
    }

    @Test
    void listsOrdersOfCountryInPeriodOldestFirst() {
        fill();

        List<SalesOrder> orders = service.orders("UK", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        assertEquals(List.of("B", "A", "C"), orders.stream().map(SalesOrder::number).toList());
        assertEquals(1, orders.get(0).lines().size());
        assertEquals(List.of(), service.orders("IT", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void countsOrdersPerCountryHighestFirst() {
        fill();

        assertEquals(List.of(new CountryOrderCount("UK", 3), new CountryOrderCount("FR", 2), new CountryOrderCount("DE", 1)),
                service.countPerCountry(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void emptyPeriodGivesEmptyResults() {
        assertEquals(List.of(), service.countPerCountry(LocalDate.of(2030, 1, 1), LocalDate.of(2030, 1, 31)));
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.countPerCountry(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.orders("UK", null, LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.orders(" ", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 2)));
    }
}
