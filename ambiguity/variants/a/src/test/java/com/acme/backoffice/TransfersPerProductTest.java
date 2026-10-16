package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.TransferRoute;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseTransfer;
import com.acme.backoffice.model.WarehouseTransferLine;
import com.acme.backoffice.service.WarehouseTransferService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransfersPerProductTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final WarehouseTransferService transfers = new WarehouseTransferService(store);
    private final Warehouse a = store.createWarehouse(new Warehouse(0, "A", "Alpha", "Oslo", Region.NORTH));
    private final Warehouse b = store.createWarehouse(new Warehouse(0, "B", "Beta", "Warsaw", Region.EAST));
    private final Product nail = store.createProduct(new Product(0, "N", "Nail", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private final Product paint = store.createProduct(new Product(0, "P", "Paint", "Paints", new BigDecimal("10.00"), "piece", true));
    private final LocalDate day = LocalDate.of(2024, 5, 10);

    @Test
    void listsTransfersAndSumsQuantityPerRoute() {
        store.setStockQuantity(a.id(), nail.id(), 100);
        store.setStockQuantity(a.id(), paint.id(), 50);
        store.setStockQuantity(b.id(), nail.id(), 100);
        transfers.transfer("T1", a.id(), b.id(), day, List.of(new WarehouseTransferLine(nail.id(), 10),
                new WarehouseTransferLine(paint.id(), 4)));
        transfers.transfer("T2", b.id(), a.id(), day.plusDays(1), List.of(new WarehouseTransferLine(nail.id(), 3)));
        transfers.transfer("T3", a.id(), b.id(), day.plusDays(2), List.of(new WarehouseTransferLine(nail.id(), 7)));
        transfers.transfer("T4", a.id(), b.id(), day.plusDays(3), List.of(new WarehouseTransferLine(paint.id(), 5)));
        transfers.transfer("T5", a.id(), b.id(), day.plusDays(30), List.of(new WarehouseTransferLine(nail.id(), 20)));

        List<WarehouseTransfer> list = transfers.listByProduct(nail.id(), day, day.plusDays(5));
        assertEquals(List.of("T1", "T2", "T3"), list.stream().map(WarehouseTransfer::number).toList());
        assertEquals(2, list.get(0).lines().size());

        List<TransferRoute> routes = transfers.quantityPerRoute(nail.id(), day, day.plusDays(5));
        assertEquals(2, routes.size());
        assertEquals("A", routes.get(0).fromWarehouseCode());
        assertEquals("B", routes.get(0).toWarehouseCode());
        assertEquals(17, routes.get(0).quantity());
        assertEquals("B", routes.get(1).fromWarehouseCode());
        assertEquals(3, routes.get(1).quantity());
    }

    @Test
    void emptyResultAndInvalidArgumentsFail() {
        assertEquals(List.of(), transfers.listByProduct(nail.id(), day, day));
        assertEquals(List.of(), transfers.quantityPerRoute(nail.id(), day, day));
        assertThrows(IllegalArgumentException.class, () -> transfers.listByProduct(nail.id(), day, day.minusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> transfers.quantityPerRoute(nail.id(), null, day));
        assertThrows(IllegalArgumentException.class, () -> transfers.quantityPerRoute(nail.id(), day, null));
    }
}
