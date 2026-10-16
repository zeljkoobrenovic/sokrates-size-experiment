package com.acme.backoffice;

import com.acme.backoffice.model.BundleComponent;
import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.ServiceLevel;
import com.acme.backoffice.model.StockMovement;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.BundleService;
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

class BundleTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final BundleService bundles = new BundleService(store);
    private final StockService stock = new StockService(store);
    private final ShipmentService shipments = new ShipmentService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 1);
    private Customer customer;
    private Warehouse north;
    private Warehouse east;
    private Product widget;
    private Product gadget;
    private Product kit;

    @BeforeEach
    void setUp() {
        customer = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
        widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        gadget = store.createProduct(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("30.00"), "piece", true));
        kit = store.createProduct(new Product(0, "K-1", "Kit", "Kits", new BigDecimal("40.00"), "piece", true));
        north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        east = store.createWarehouse(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));
        store.createCarrier(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50")));
        bundles.addComponent(kit.id(), widget.id(), 4);
        bundles.addComponent(kit.id(), gadget.id(), 1);
    }

    @Test
    void listsBundlesAndComponents() {
        assertEquals(List.of(kit), bundles.listBundles());
        List<BundleComponent> components = bundles.getComponents(kit.id());
        assertEquals(List.of("Gadget", "Widget"), components.stream().map(c -> c.component().name()).toList());
        assertEquals(List.of(1, 4), components.stream().map(BundleComponent::quantity).toList());
        assertTrue(bundles.getComponents(widget.id()).isEmpty());
    }

    @Test
    void stockIsHowManyCanBeBuilt() {
        stock.setQuantity(north.id(), widget.id(), 10);
        stock.setQuantity(north.id(), gadget.id(), 5);
        stock.setQuantity(east.id(), widget.id(), 8);
        assertEquals(2, bundles.getQuantity(north.id(), kit.id()));
        assertEquals(0, bundles.getQuantity(east.id(), kit.id()));
        assertEquals(2, bundles.getTotalQuantity(kit.id()));
        stock.setQuantity(east.id(), gadget.id(), 3);
        assertEquals(2, bundles.getQuantity(east.id(), kit.id()));
        assertEquals(4, bundles.getTotalQuantity(kit.id()));
        assertEquals(0, bundles.getQuantity(north.id(), widget.id()));
    }

    @Test
    void shippingTakesComponentStock() {
        stock.setQuantity(north.id(), widget.id(), 10);
        stock.setQuantity(north.id(), gadget.id(), 5);
        store.createSalesOrder(new SalesOrder(0, "SO-1", customer.id(), day, OrderStatus.CONFIRMED,
                List.of(new OrderLine(kit.id(), 2, new BigDecimal("40.00")), new OrderLine(widget.id(), 1, new BigDecimal("4.50")))));
        shipments.ship("SO-1", "WH-N", "FastFreight", "FF-1", day);
        assertEquals(1, stock.getQuantity(north.id(), widget.id()));
        assertEquals(3, stock.getQuantity(north.id(), gadget.id()));
        assertEquals(0, stock.getQuantity(north.id(), kit.id()));
        assertEquals(List.of(-9), stock.getMovements(widget.id(), north.id(), day, day).stream().map(StockMovement::quantityChange).toList());
    }

    @Test
    void shortComponentStockChangesNothing() {
        stock.setQuantity(north.id(), widget.id(), 10);
        stock.setQuantity(north.id(), gadget.id(), 1);
        store.createSalesOrder(new SalesOrder(0, "SO-1", customer.id(), day, OrderStatus.CONFIRMED,
                List.of(new OrderLine(kit.id(), 2, new BigDecimal("40.00")))));
        assertThrows(IllegalStateException.class, () -> shipments.ship("SO-1", "WH-N", "FastFreight", "FF-1", day));
        assertEquals(10, stock.getQuantity(north.id(), widget.id()));
        assertEquals(1, stock.getQuantity(north.id(), gadget.id()));
    }

    @Test
    void partShipmentTakesComponentStock() {
        stock.setQuantity(north.id(), widget.id(), 10);
        stock.setQuantity(north.id(), gadget.id(), 5);
        store.createSalesOrder(new SalesOrder(0, "SO-1", customer.id(), day, OrderStatus.CONFIRMED,
                List.of(new OrderLine(kit.id(), 3, new BigDecimal("40.00")))));
        shipments.shipPart("SO-1", 1, 2, "WH-N", "FastFreight", "FF-1", day);
        assertEquals(2, stock.getQuantity(north.id(), widget.id()));
        assertEquals(3, stock.getQuantity(north.id(), gadget.id()));
        assertThrows(IllegalStateException.class, () -> shipments.shipPart("SO-1", 1, 1, "WH-N", "FastFreight", "FF-2", day));
        assertEquals(2, stock.getQuantity(north.id(), widget.id()));
        assertEquals(3, stock.getQuantity(north.id(), gadget.id()));
    }

    @Test
    void rejectsInvalidComponents() {
        Product other = store.createProduct(new Product(0, "O-1", "Other", "Hardware", new BigDecimal("1.00"), "piece", true));
        assertThrows(IllegalArgumentException.class, () -> bundles.addComponent(kit.id(), other.id(), 0));
        assertThrows(IllegalArgumentException.class, () -> bundles.addComponent(other.id(), other.id(), 1));
        assertThrows(IllegalStateException.class, () -> bundles.addComponent(kit.id(), widget.id(), 1));
        assertThrows(IllegalStateException.class, () -> bundles.addComponent(other.id(), kit.id(), 1));
        assertThrows(IllegalStateException.class, () -> bundles.addComponent(widget.id(), other.id(), 1));
    }
}
