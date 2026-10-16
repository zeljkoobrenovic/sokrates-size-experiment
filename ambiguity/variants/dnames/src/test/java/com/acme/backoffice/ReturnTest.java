package com.acme.backoffice;

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
import com.acme.backoffice.service.ReturnService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReturnTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ReturnService returns = new ReturnService(store);
    private final LocalDate day = LocalDate.of(2024, 7, 10);
    private final Customer zeta = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product gadget = store.createProduct(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("30.00"), "piece", true));
    private final Product other = store.createProduct(new Product(0, "O-1", "Other", "Hardware", new BigDecimal("1.00"), "piece", true));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Warehouse east = store.createWarehouse(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));

    private void order(String number, Customer customer) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), day, OrderStatus.DELIVERED,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal("4.50")), new OrderLine(gadget.id(), 2, new BigDecimal("30.00")))));
    }

    @Test
    void returnIsStoredAndAddsStockToTheWarehouse() {
        order("SO-1", zeta);
        store.setStockQuantity(north.id(), widget.id(), 5);
        SalesReturn created = returns.createReturn("R-1", "SO-1", day.plusDays(3), "Damaged", "WH-N",
                List.of(new ReturnLine(widget.id(), 4), new ReturnLine(gadget.id(), 1)));
        assertTrue(created.id() > 0);
        assertEquals(9, store.findStockQuantityInWarehouse(north.id(), widget.id()));
        assertEquals(1, store.findStockQuantityInWarehouse(north.id(), gadget.id()));
        assertEquals(0, store.findStockQuantityInAllWarehouses(other.id()));
        assertEquals(0, store.findStockQuantityInWarehouse(east.id(), widget.id()));
        SalesReturn found = returns.findReturn("R-1").orElseThrow();
        assertEquals("Damaged", found.reason());
        assertEquals(day.plusDays(3), found.returnDate());
        assertEquals(List.of(new ReturnLine(widget.id(), 4), new ReturnLine(gadget.id(), 1)), found.lines());
        assertTrue(returns.findReturn("R-404").isEmpty());
    }

    @Test
    void listsReturnsOfAnOrderAndOfACustomer() {
        order("SO-1", zeta);
        order("SO-2", zeta);
        order("SO-3", alpha);
        returns.createReturn("R-2", "SO-1", day.plusDays(5), "Wrong item", "WH-N", List.of(new ReturnLine(widget.id(), 1)));
        returns.createReturn("R-1", "SO-1", day.plusDays(2), "Damaged", "WH-N", List.of(new ReturnLine(widget.id(), 1)));
        returns.createReturn("R-3", "SO-2", day.plusDays(3), "Damaged", "WH-E", List.of(new ReturnLine(gadget.id(), 1)));
        returns.createReturn("R-4", "SO-3", day.plusDays(3), "Damaged", "WH-E", List.of(new ReturnLine(gadget.id(), 1)));
        assertEquals(List.of("R-1", "R-2"), returns.listByOrder("SO-1").stream().map(SalesReturn::number).toList());
        assertEquals(List.of("R-1", "R-3", "R-2"), returns.listByCustomer(zeta.id()).stream().map(SalesReturn::number).toList());
        assertEquals(List.of("R-4"), returns.listByCustomer(alpha.id()).stream().map(SalesReturn::number).toList());
        assertTrue(returns.listByCustomer(0).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> returns.listByOrder("SO-404"));
    }

    @Test
    void invalidReturnsAreRejectedAndChangeNothing() {
        order("SO-1", zeta);
        List<ReturnLine> ok = List.of(new ReturnLine(widget.id(), 1));
        assertThrows(IllegalArgumentException.class, () -> returns.createReturn("R-1", "SO-404", day, "x", "WH-N", ok));
        assertThrows(IllegalArgumentException.class, () -> returns.createReturn("R-1", "SO-1", day, "x", "WH-404", ok));
        assertThrows(IllegalArgumentException.class, () -> returns.createReturn("R-1", "SO-1", null, "x", "WH-N", ok));
        assertThrows(IllegalArgumentException.class, () -> returns.createReturn("R-1", "SO-1", day, " ", "WH-N", ok));
        assertThrows(IllegalArgumentException.class, () -> returns.createReturn("R-1", "SO-1", day, "x", "WH-N", List.of()));
        assertThrows(IllegalArgumentException.class, () -> returns.createReturn("R-1", "SO-1", day, "x", "WH-N",
                List.of(new ReturnLine(widget.id(), 0))));
        assertThrows(IllegalArgumentException.class, () -> returns.createReturn("R-1", "SO-1", day, "x", "WH-N",
                List.of(new ReturnLine(other.id(), 1))));
        assertThrows(IllegalArgumentException.class, () -> returns.createReturn("R-1", "SO-1", day, "x", "WH-N",
                List.of(new ReturnLine(widget.id(), 1), new ReturnLine(gadget.id(), 3))));
        assertTrue(returns.findReturn("R-1").isEmpty());
        assertEquals(0, store.findStockQuantityInAllWarehouses(widget.id()));
    }

    @Test
    void cannotReturnMoreThanOrderedInTotalOrReuseANumber() {
        order("SO-1", zeta);
        returns.createReturn("R-1", "SO-1", day, "x", "WH-N", List.of(new ReturnLine(widget.id(), 8)));
        assertThrows(IllegalArgumentException.class, () -> returns.createReturn("R-2", "SO-1", day, "x", "WH-N",
                List.of(new ReturnLine(widget.id(), 3))));
        assertThrows(IllegalArgumentException.class, () -> returns.createReturn("R-1", "SO-1", day, "x", "WH-N",
                List.of(new ReturnLine(widget.id(), 1))));
        returns.createReturn("R-3", "SO-1", day, "x", "WH-N", List.of(new ReturnLine(widget.id(), 2)));
        assertEquals(10, store.findStockQuantityInWarehouse(north.id(), widget.id()));
    }
}
