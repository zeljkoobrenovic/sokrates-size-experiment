package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.ServiceLevel;
import com.acme.backoffice.model.StockMovement;
import com.acme.backoffice.model.StockMovementReason;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.ReturnService;
import com.acme.backoffice.service.ShipmentService;
import com.acme.backoffice.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockMovementTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final StockService stock = new StockService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 10);
    private Warehouse north;
    private Warehouse east;
    private Product widget;
    private Product gadget;

    @BeforeEach
    void setUp() {
        widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        gadget = store.createProduct(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("30.00"), "piece", true));
        north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        east = store.createWarehouse(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));
    }

    private List<Integer> changes(List<StockMovement> movements) {
        return movements.stream().map(StockMovement::quantityChange).toList();
    }

    @Test
    void adjustmentsAreLogged() {
        stock.setQuantity(north.id(), widget.id(), 10, day);
        stock.addQuantity(north.id(), widget.id(), 5, day.plusDays(1));
        stock.takeQuantity(north.id(), widget.id(), 3, day.plusDays(2));
        stock.setQuantity(north.id(), widget.id(), 4, day.plusDays(3));
        stock.setQuantity(north.id(), widget.id(), 4, day.plusDays(4));

        List<StockMovement> movements = stock.getMovements(widget.id(), north.id(), day, day.plusDays(10));
        assertEquals(List.of(10, 5, -3, -8), changes(movements));
        assertEquals(StockMovementReason.ADJUSTMENT, movements.get(0).reason());
        assertEquals(day, movements.get(0).movementDate());
        assertEquals(widget.id(), movements.get(0).productId());
        assertEquals(north.id(), movements.get(0).warehouseId());
        assertEquals(4, stock.getQuantity(north.id(), widget.id()));
    }

    @Test
    void failedTakeIsNotLogged() {
        stock.setQuantity(north.id(), widget.id(), 2, day);
        assertThrows(IllegalStateException.class, () -> stock.takeQuantity(north.id(), widget.id(), 5, day));
        assertEquals(1, stock.getProductMovements(widget.id(), day, day).size());
    }

    @Test
    void shipmentAndReturnAreLogged() {
        Customer customer = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
        store.createCarrier(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50")));
        stock.setQuantity(north.id(), widget.id(), 10);
        store.createSalesOrder(new SalesOrder(0, "SO-1", customer.id(), day, OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 4, new BigDecimal("4.50")), new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));
        new ShipmentService(store).ship("SO-1", "WH-N", "FastFreight", "FF-1", day.plusDays(1));
        new ReturnService(store).createReturn("R-1", "SO-1", day.plusDays(5), "Damaged", "WH-E",
                List.of(new ReturnLine(widget.id(), 2)));

        List<StockMovement> movements = stock.getProductMovements(widget.id(), day, day.plusDays(10));
        assertEquals(List.of(-6, 2), changes(movements));
        assertEquals(StockMovementReason.SHIPMENT, movements.get(0).reason());
        assertEquals(north.id(), movements.get(0).warehouseId());
        assertEquals(StockMovementReason.RETURN, movements.get(1).reason());
        assertEquals(east.id(), movements.get(1).warehouseId());
        assertEquals(day.plusDays(5), movements.get(1).movementDate());
    }

    @Test
    void listsByProductWarehouseAndPeriod() {
        stock.addQuantity(north.id(), widget.id(), 1, day.minusDays(1));
        stock.addQuantity(north.id(), widget.id(), 2, day);
        stock.addQuantity(east.id(), widget.id(), 4, day.plusDays(2));
        stock.addQuantity(north.id(), gadget.id(), 8, day.plusDays(2));
        stock.addQuantity(east.id(), gadget.id(), 16, day.plusDays(3));
        stock.addQuantity(north.id(), widget.id(), 32, day.plusDays(4));

        assertEquals(List.of(2, 4), changes(stock.getProductMovements(widget.id(), day, day.plusDays(3))));
        assertEquals(List.of(2, 8), changes(stock.getWarehouseMovements(north.id(), day, day.plusDays(3))));
        assertEquals(List.of(2), changes(stock.getMovements(widget.id(), north.id(), day, day.plusDays(3))));
        assertEquals(List.of(1, 2, 32), changes(stock.getMovements(widget.id(), north.id(), day.minusDays(1), day.plusDays(4))));
    }

    @Test
    void nothingInPeriod() {
        stock.addQuantity(north.id(), widget.id(), 1, day);
        assertTrue(stock.getProductMovements(widget.id(), day.plusDays(1), day.plusDays(2)).isEmpty());
        assertTrue(stock.getWarehouseMovements(east.id(), day, day).isEmpty());
        assertTrue(stock.getMovements(gadget.id(), north.id(), day, day).isEmpty());
    }

    @Test
    void dateIsRequired() {
        assertThrows(IllegalArgumentException.class, () -> stock.addQuantity(north.id(), widget.id(), 1, null));
    }
}
