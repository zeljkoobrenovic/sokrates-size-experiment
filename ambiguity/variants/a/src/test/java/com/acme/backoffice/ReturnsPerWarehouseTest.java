package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseReturns;
import com.acme.backoffice.service.ReturnService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReturnsPerWarehouseTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ReturnService returns = new ReturnService(store);
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product gadget = store.createProduct(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("6.00"), "piece", true));

    private void ret(String number, String date, String warehouse, ReturnLine... lines) {
        returns.createReturn(number, "SO-1", LocalDate.parse(date), "Damaged", warehouse, List.of(lines));
    }

    private void setUp() {
        store.createWarehouse(new Warehouse(0, "WH1", "Main", "Riga", Region.NORTH));
        store.createWarehouse(new Warehouse(0, "WH2", "Second", "Tallinn", Region.NORTH));
        Customer customer = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.createSalesOrder(new SalesOrder(0, "SO-1", customer.id(), LocalDate.of(2024, 1, 1), OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 50, new BigDecimal("2.50")), new OrderLine(gadget.id(), 50, new BigDecimal("3.00")))));
    }

    @Test
    void listsReturnsAndQuantitiesOfOneWarehouseInThePeriod() {
        setUp();
        ret("R3", "2024-03-20", "WH1", new ReturnLine(widget.id(), 3));
        ret("R1", "2024-03-01", "WH1", new ReturnLine(widget.id(), 2), new ReturnLine(gadget.id(), 7));
        ret("R2", "2024-03-31", "WH2", new ReturnLine(widget.id(), 9));
        ret("R4", "2024-04-01", "WH1", new ReturnLine(widget.id(), 11));
        ret("R0", "2024-02-29", "WH1", new ReturnLine(widget.id(), 13));

        WarehouseReturns result = returns.listReturnsByWarehouse("WH1", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        assertEquals("WH1", result.warehouse().code());
        assertEquals(List.of("R1", "R3"), result.returns().stream().map(r -> r.number()).toList());
        assertEquals("SO-1", result.returns().get(0).orderNumber());
        assertEquals(List.of("G-1:7", "W-1:5"), result.quantities().stream().map(q -> q.sku() + ":" + q.quantity()).toList());
    }

    @Test
    void returnsNothingForAWarehouseWithoutReturns() {
        setUp();
        WarehouseReturns result = returns.listReturnsByWarehouse("WH2", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of(), result.returns());
        assertEquals(List.of(), result.quantities());
    }

    @Test
    void rejectsUnknownWarehouseAndBadDates() {
        setUp();
        assertThrows(IllegalArgumentException.class, () -> returns.listReturnsByWarehouse("NOPE", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)));
        assertThrows(IllegalArgumentException.class, () -> returns.listReturnsByWarehouse("WH1", LocalDate.of(2024, 3, 31), LocalDate.of(2024, 3, 1)));
    }
}
