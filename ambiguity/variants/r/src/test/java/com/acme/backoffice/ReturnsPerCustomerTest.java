package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerReturns;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.ReturnService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReturnsPerCustomerTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ReturnService returns = new ReturnService(store);
    private final Customer alpha = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.customers().create(new Customer(0, "Beta", "b@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
    private final Customer gamma = store.customers().create(new Customer(0, "Gamma", "g@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private void order(String number, Customer customer) {
        store.orders().create(new SalesOrder(0, number, customer.id(), LocalDate.of(2024, 1, 1), OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 20, new BigDecimal("2.50")))));
    }

    private void ret(String number, String order, String date, int quantity) {
        returns.createReturn(number, order, LocalDate.parse(date), "Damaged", "WH", List.of(new ReturnLine(widget.id(), quantity)));
    }

    private List<String> lines(List<CustomerReturns> result) {
        return result.stream().flatMap(c -> c.returns().stream()
                .map(r -> c.customerName() + ":" + r.number() + ":" + r.returnedValue())).toList();
    }

    @Test
    void groupsReturnsPerCustomerWithMostReturnsFirst() {
        store.warehouses().create(new Warehouse(0, "WH", "Main", "Riga", Region.NORTH));
        order("SO-A", alpha);
        order("SO-B", beta);
        order("SO-G", gamma);
        ret("R-A1", "SO-A", "2024-03-05", 2);
        ret("R-B1", "SO-B", "2024-03-20", 1);
        ret("R-B2", "SO-B", "2024-03-10", 4);
        ret("R-B3", "SO-B", "2024-03-01", 3);
        ret("R-G1", "SO-G", "2024-04-01", 5);

        List<CustomerReturns> result = returns.listReturnsPerCustomer(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        assertEquals(List.of("Beta:R-B3:7.50", "Beta:R-B2:10.00", "Beta:R-B1:2.50", "Alpha:R-A1:5.00"), lines(result));
        assertEquals(2, result.size());
        assertEquals(new BigDecimal("20.00"), result.get(0).returnedValue());
        assertEquals(new BigDecimal("5.00"), result.get(1).returnedValue());
    }

    @Test
    void returnsNothingWithoutReturnsInThePeriod() {
        assertEquals(List.of(), returns.listReturnsPerCustomer(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)));
    }
}
