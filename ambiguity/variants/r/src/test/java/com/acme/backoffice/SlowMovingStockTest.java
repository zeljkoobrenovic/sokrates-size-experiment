package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.ServiceLevel;
import com.acme.backoffice.model.Shipment;
import com.acme.backoffice.model.SlowMovingStock;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseSlowMovingStock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlowMovingStockTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final LocalDate date = LocalDate.of(2024, 6, 30);
    private final Warehouse a = store.warehouses().create(new Warehouse(0, "A", "Alpha", "Oslo", Region.NORTH));
    private final Warehouse b = store.warehouses().create(new Warehouse(0, "B", "Beta", "Warsaw", Region.EAST));
    private final Product nail = store.products().create(new Product(0, "N", "Nail", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private final Product paint = store.products().create(new Product(0, "P", "Paint", "Paints", new BigDecimal("10.00"), "piece", true));
    private final Product tool = store.products().create(new Product(0, "T", "Tool", "Tools", new BigDecimal("20.00"), "piece", true));
    private final Product lamp = store.products().create(new Product(0, "L", "Lamp", "Lamps", new BigDecimal("30.00"), "piece", true));
    private final Carrier carrier = store.carriers().create(new Carrier(0, "Fast", ServiceLevel.STANDARD, new BigDecimal("1.00")));
    private final Customer customer = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));

    private SalesOrder order(String number, Product product) {
        return store.orders().create(new SalesOrder(0, number, customer.id(), LocalDate.of(2024, 1, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(product.id(), 1, new BigDecimal("1.00")))));
    }

    private void setUp() {
        Supplier supplier = store.suppliers().create(new Supplier(0, "Sup", "s@example.com", "Sweden", 3));
        store.purchaseOrders().create(new PurchaseOrder(0, "PO-1", supplier.id(), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5),
                PurchaseOrderStatus.RECEIVED, List.of(new PurchaseOrderLine(nail.id(), 10, new BigDecimal("0.50")),
                new PurchaseOrderLine(paint.id(), 10, new BigDecimal("6.00")), new PurchaseOrderLine(tool.id(), 10, new BigDecimal("12.00")))));
        store.stock().setQuantity(a.id(), nail.id(), 40);
        store.stock().setQuantity(a.id(), paint.id(), 5);
        store.stock().setQuantity(b.id(), paint.id(), 10);
        store.stock().setQuantity(b.id(), tool.id(), 3);
        store.stock().setQuantity(b.id(), lamp.id(), 0);
        // paint shipped from A inside the window, nail shipped from A before the window, tool shipped from A on the date itself
        store.shipments().create(new Shipment(0, order("SO-1", paint).id(), a.id(), carrier.id(), "T1", date.minusDays(10)));
        store.shipments().create(new Shipment(0, order("SO-2", nail).id(), a.id(), carrier.id(), "T2", date.minusDays(31)));
        store.shipments().create(new Shipment(0, order("SO-3", tool).id(), a.id(), carrier.id(), "T3", date));
    }

    @Test
    void listsProductsNotShippedFromTheWarehouseInTheWindow() {
        setUp();

        assertEquals(List.of(
                new WarehouseSlowMovingStock(a, nail, 40, new BigDecimal("20.00")),
                new WarehouseSlowMovingStock(b, paint, 10, new BigDecimal("60.00")),
                new WarehouseSlowMovingStock(b, tool, 3, new BigDecimal("36.00"))), store.stock().listSlowMovingPerWarehouse(date, 30));
    }

    @Test
    void listsProductsNotShippedFromAnyWarehouseInTheWindow() {
        setUp();

        assertEquals(List.of(
                new SlowMovingStock(tool, 3, new BigDecimal("36.00")),
                new SlowMovingStock(nail, 40, new BigDecimal("20.00"))), store.stock().listSlowMoving(date, 30));
    }

    @Test
    void windowStartsOnTheFirstDayAndCountsPartialShipments() {
        setUp();
        SalesOrder partial = order("SO-4", nail);
        store.shipments().createPart(partial.id(), 1, b.id(), carrier.id(), "T4", date.minusDays(30), 1);

        assertEquals(List.of(new SlowMovingStock(tool, 3, new BigDecimal("36.00"))), store.stock().listSlowMoving(date, 30));
        assertEquals(List.of(
                new WarehouseSlowMovingStock(a, nail, 40, new BigDecimal("20.00")),
                new WarehouseSlowMovingStock(b, paint, 10, new BigDecimal("60.00")),
                new WarehouseSlowMovingStock(b, tool, 3, new BigDecimal("36.00"))), store.stock().listSlowMovingPerWarehouse(date, 30));
    }
}
