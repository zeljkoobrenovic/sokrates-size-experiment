package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.StockMovement;
import com.acme.backoffice.model.StockMovementReason;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseTransfer;
import com.acme.backoffice.model.WarehouseTransferLine;
import com.acme.backoffice.service.StockService;
import com.acme.backoffice.service.WarehouseTransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WarehouseTransferTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final StockService stock = new StockService(store);
    private final WarehouseTransferService transfers = new WarehouseTransferService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 10);
    private Warehouse north;
    private Warehouse east;
    private Warehouse south;
    private Product widget;
    private Product gadget;

    @BeforeEach
    void setUp() {
        widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        gadget = store.products().create(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("30.00"), "piece", true));
        north = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        east = store.warehouses().create(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));
        south = store.warehouses().create(new Warehouse(0, "WH-S", "South", "Rome", Region.SOUTH));
    }

    @Test
    void transferMovesStockAndLogsBothMovements() {
        stock.setQuantity(north.id(), widget.id(), 10);
        stock.setQuantity(north.id(), gadget.id(), 4);
        stock.setQuantity(east.id(), widget.id(), 2);

        WarehouseTransfer created = transfers.transfer("TR-1", north.id(), east.id(), day,
                List.of(new WarehouseTransferLine(widget.id(), 6), new WarehouseTransferLine(gadget.id(), 4)));
        assertTrue(created.id() > 0);

        assertEquals(4, stock.getQuantity(north.id(), widget.id()));
        assertEquals(8, stock.getQuantity(east.id(), widget.id()));
        assertEquals(0, stock.getQuantity(north.id(), gadget.id()));
        assertEquals(4, stock.getQuantity(east.id(), gadget.id()));

        List<StockMovement> out = stock.getMovements(widget.id(), north.id(), day, day);
        List<StockMovement> in = stock.getMovements(widget.id(), east.id(), day, day);
        assertEquals(List.of(-6), out.stream().map(StockMovement::quantityChange).toList());
        assertEquals(List.of(6), in.stream().map(StockMovement::quantityChange).toList());
        assertEquals(StockMovementReason.TRANSFER, out.get(0).reason());
        assertEquals(StockMovementReason.TRANSFER, in.get(0).reason());
    }

    @Test
    void shortStockChangesNothing() {
        stock.setQuantity(north.id(), widget.id(), 10);
        stock.setQuantity(north.id(), gadget.id(), 1);

        assertThrows(IllegalStateException.class, () -> transfers.transfer("TR-1", north.id(), east.id(), day,
                List.of(new WarehouseTransferLine(widget.id(), 5), new WarehouseTransferLine(gadget.id(), 2))));

        assertEquals(10, stock.getQuantity(north.id(), widget.id()));
        assertEquals(0, stock.getQuantity(east.id(), widget.id()));
        assertEquals(1, stock.getQuantity(north.id(), gadget.id()));
        assertTrue(stock.getMovements(widget.id(), north.id(), day, day).isEmpty());
        assertTrue(transfers.listByPeriod(day, day).isEmpty());
    }

    @Test
    void invalidInputIsRejected() {
        stock.setQuantity(north.id(), widget.id(), 10);
        List<WarehouseTransferLine> lines = List.of(new WarehouseTransferLine(widget.id(), 1));
        assertThrows(IllegalArgumentException.class, () -> transfers.transfer(" ", north.id(), east.id(), day, lines));
        assertThrows(IllegalArgumentException.class, () -> transfers.transfer("T", north.id(), east.id(), null, lines));
        assertThrows(IllegalArgumentException.class, () -> transfers.transfer("T", north.id(), north.id(), day, lines));
        assertThrows(IllegalArgumentException.class, () -> transfers.transfer("T", north.id(), east.id(), day, List.of()));
        assertThrows(IllegalArgumentException.class, () -> transfers.transfer("T", north.id(), east.id(), day,
                List.of(new WarehouseTransferLine(widget.id(), 0))));
        assertThrows(IllegalStateException.class, () -> transfers.transfer("T", north.id(), 9999, day, lines));

        transfers.transfer("T", north.id(), east.id(), day, lines);
        assertThrows(IllegalStateException.class, () -> transfers.transfer("T", north.id(), east.id(), day, lines));
        assertEquals(9, stock.getQuantity(north.id(), widget.id()));
        assertEquals(1, stock.getQuantity(east.id(), widget.id()));
    }

    @Test
    void transfersAreListedPerWarehouseAndPeriod() {
        stock.setQuantity(north.id(), widget.id(), 100);
        stock.setQuantity(east.id(), widget.id(), 100);
        List<WarehouseTransferLine> lines = List.of(new WarehouseTransferLine(widget.id(), 1));
        transfers.transfer("TR-1", north.id(), east.id(), day, lines);
        transfers.transfer("TR-2", east.id(), south.id(), day.plusDays(3), lines);
        transfers.transfer("TR-3", south.id(), north.id(), day.plusDays(10), lines);

        assertEquals(List.of("TR-1", "TR-2"), numbers(transfers.listByWarehouse(east.id(), day, day.plusDays(30))));
        assertEquals(List.of("TR-1", "TR-3"), numbers(transfers.listByWarehouse(north.id(), day, day.plusDays(30))));
        assertEquals(List.of("TR-1"), numbers(transfers.listByWarehouse(north.id(), day, day.plusDays(5))));
        assertEquals(List.of("TR-2", "TR-3"), numbers(transfers.listByPeriod(day.plusDays(3), day.plusDays(10))));
        assertEquals(3, numbers(transfers.listByPeriod(day, day.plusDays(10))).size());
        assertTrue(transfers.listByPeriod(day.plusDays(11), day.plusDays(20)).isEmpty());
        assertEquals(widget.id(), transfers.listByPeriod(day, day).get(0).lines().get(0).productId());
    }

    private static List<String> numbers(List<WarehouseTransfer> list) {
        return list.stream().map(WarehouseTransfer::number).toList();
    }
}
