package com.acme.backoffice;

import com.acme.backoffice.model.BackOrder;
import com.acme.backoffice.model.CategoryBackOrders;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.BackOrderService;
import com.acme.backoffice.service.BundleService;
import com.acme.backoffice.service.OrderService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BackOrdersPerWarehouseTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final BackOrderService service = new BackOrderService(store);
    private final Customer customer = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Warehouse east = store.createWarehouse(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));
    private final Product paint = store.createProduct(new Product(0, "P-1", "Paint", "Paints", new BigDecimal("5.00"), "piece", true));
    private final Product tool = store.createProduct(new Product(0, "T-1", "Tool", "Tools", new BigDecimal("7.00"), "piece", true));

    private void backOrder(String number, int day, OrderLine... lines) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), LocalDate.of(2024, 1, 1), OrderStatus.CONFIRMED, List.of(lines)));
        service.putOnBackOrder(number, LocalDate.of(2024, 2, day));
    }

    private OrderLine line(Product product, int quantity) {
        return new OrderLine(product.id(), quantity, new BigDecimal("1.00"));
    }

    private List<String> numbers(List<BackOrder> backOrders) {
        return backOrders.stream().map(BackOrder::orderNumber).toList();
    }

    @Test
    void listsBackOrdersTheWarehouseCouldShipFromItsOwnStock() {
        backOrder("SO-2", 2, line(paint, 2), line(tool, 3));
        backOrder("SO-1", 1, line(paint, 4));
        backOrder("SO-3", 3, line(tool, 50));
        store.setStockQuantity(north.id(), paint.id(), 5);
        store.setStockQuantity(east.id(), paint.id(), 3);
        store.setStockQuantity(east.id(), tool.id(), 3);

        assertEquals(List.of("SO-1"), numbers(service.listShippableFrom("WH-N")));
        assertEquals(List.of("SO-2"), numbers(service.listShippableFrom("WH-E")));
    }

    @Test
    void shippedOrBundledBackOrders() {
        Product kit = store.createProduct(new Product(0, "K-1", "Kit", "Kits", new BigDecimal("20.00"), "piece", true));
        new BundleService(store).addComponent(kit.id(), paint.id(), 2);
        backOrder("SO-1", 1, line(kit, 2));
        backOrder("SO-2", 2, line(paint, 1));
        store.setStockQuantity(north.id(), paint.id(), 4);
        assertEquals(List.of("SO-1", "SO-2"), numbers(service.listShippableFrom("WH-N")));
        assertTrue(service.listShippableFrom("WH-E").isEmpty());

        new OrderService(store).cancel("SO-1", com.acme.backoffice.model.CancellationReason.CUSTOMER_REQUEST, LocalDate.of(2024, 3, 1));
        assertEquals(List.of("SO-2"), numbers(service.listShippableFrom("WH-N")));
    }

    @Test
    void unknownWarehouseIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.listShippableFrom("WH-X"));
    }

    @Test
    void countsBackOrdersPerCategory() {
        backOrder("SO-1", 1, line(paint, 4), line(paint, 1));
        backOrder("SO-2", 2, line(paint, 2), line(tool, 3));
        backOrder("SO-3", 3, line(tool, 50));

        List<CategoryBackOrders> result = service.listPerCategory();
        assertEquals(2, result.size());
        assertEquals(new CategoryBackOrders("Paints", 2, 7), result.get(0));
        assertEquals(new CategoryBackOrders("Tools", 2, 53), result.get(1));
    }

    @Test
    void noBackOrders() {
        assertTrue(service.listPerCategory().isEmpty());
        assertTrue(service.listShippableFrom("WH-N").isEmpty());
    }
}
