package com.acme.backoffice;

import com.acme.backoffice.model.CountryReturnCount;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.CountryReturnsService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountryReturnsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CountryReturnsService service = new CountryReturnsService(store);
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse warehouse = store.warehouses().create(new Warehouse(0, "WH-1", "Main", "Oslo", Region.NORTH));
    private int counter;

    private Customer customer(String country) {
        counter++;
        return store.customers().create(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, country, Region.NORTH, CustomerStatus.ACTIVE));
    }

    private void ret(String number, Customer customer, LocalDate date) {
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + number, customer.id(), date, OrderStatus.DELIVERED,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("10.00")))));
        store.returns().create(new SalesReturn(0, number, order.id(), date, "Damaged",
                List.of(new ReturnLine(widget.id(), 1))), warehouse.id());
    }

    private void fill() {
        Customer uk1 = customer("UK");
        Customer uk2 = customer("UK");
        Customer de = customer("DE");
        Customer fr = customer("FR");
        ret("A", uk1, LocalDate.of(2024, 1, 20));
        ret("B", uk2, LocalDate.of(2024, 1, 1));
        ret("C", uk1, LocalDate.of(2024, 1, 31));
        ret("D", uk1, LocalDate.of(2023, 12, 31));
        ret("E", uk1, LocalDate.of(2024, 2, 1));
        ret("G", de, LocalDate.of(2024, 1, 10));
        ret("H", fr, LocalDate.of(2024, 1, 11));
        ret("I", fr, LocalDate.of(2024, 1, 12));
    }

    @Test
    void listsReturnsOfCountryInPeriodOldestFirst() {
        fill();

        List<SalesReturn> returns = service.returns("UK", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        assertEquals(List.of("B", "A", "C"), returns.stream().map(SalesReturn::number).toList());
        assertEquals(1, returns.get(0).lines().size());
        assertEquals(List.of(), service.returns("IT", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void countsReturnsPerCountryHighestFirst() {
        fill();

        assertEquals(List.of(new CountryReturnCount("UK", 3), new CountryReturnCount("FR", 2), new CountryReturnCount("DE", 1)),
                service.countPerCountry(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void emptyPeriodGivesEmptyResults() {
        assertEquals(List.of(), service.countPerCountry(LocalDate.of(2030, 1, 1), LocalDate.of(2030, 1, 31)));
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.countPerCountry(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.returns("UK", null, LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.returns(" ", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 2)));
    }
}
