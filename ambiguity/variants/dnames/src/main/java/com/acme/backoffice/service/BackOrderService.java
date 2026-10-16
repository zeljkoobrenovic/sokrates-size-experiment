package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.BackOrder;
import com.acme.backoffice.model.BundleComponent;
import com.acme.backoffice.model.CategoryBackOrders;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Warehouse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Confirmed orders that wait for stock. */
public class BackOrderService {
    private final DataStore store;

    public BackOrderService(DataStore store) {
        this.store = store;
    }

    /**
     * Puts a confirmed order on back order. Fails with IllegalStateException, changing nothing, if the order is not
     * confirmed, is already on back order, or the total stock of all warehouses covers every ordered product.
     */
    public BackOrder putOnBackOrder(String orderNumber, LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        SalesOrder order = store.findSalesOrderByNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unknown order: " + orderNumber));
        if (order.status() != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Order " + orderNumber + " cannot be put on back order, it is " + order.status());
        }
        if (store.findBackOrderOfOrder(order.id()).isPresent()) {
            throw new IllegalStateException("Order " + orderNumber + " is already on back order");
        }
        Map<Long, Integer> needed = new LinkedHashMap<>();
        for (OrderLine line : order.lines()) {
            needed.merge(line.productId(), line.quantity(), Integer::sum);
        }
        boolean stockShort = false;
        for (Map.Entry<Long, Integer> entry : needed.entrySet()) {
            if (store.findStockQuantityInAllWarehouses(entry.getKey()) < entry.getValue()) {
                stockShort = true;
            }
        }
        if (!stockShort) {
            throw new IllegalStateException("Order " + orderNumber + " can be shipped, the stock is sufficient");
        }
        return store.createBackOrder(order.id(), date);
    }

    /** All back orders that are still waiting (the order is still confirmed), oldest first. */
    public List<BackOrder> listAll() {
        return store.listWaitingBackOrders();
    }

    /** The waiting back orders of the customer, oldest first. */
    public List<BackOrder> listByCustomer(long customerId) {
        return store.listWaitingBackOrdersOfCustomer(customerId);
    }

    /** The waiting back orders with a line for the product, oldest first. */
    public List<BackOrder> listByProduct(long productId) {
        return store.listWaitingBackOrdersWithProduct(productId);
    }

    /**
     * The waiting back orders the warehouse could ship now: its own stock covers all lines at once (bundles count by their
     * components, as when shipping). Each back order is judged on its own, stock is not reserved between them. Oldest first.
     */
    public List<BackOrder> listShippableFrom(String warehouseCode) {
        Warehouse warehouse = store.findWarehouseByCode(warehouseCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown warehouse: " + warehouseCode));
        List<BackOrder> result = new ArrayList<>();
        for (BackOrder backOrder : store.listWaitingBackOrders()) {
            SalesOrder order = store.findSalesOrderByNumber(backOrder.orderNumber()).orElseThrow();
            Map<Long, Integer> needed = new LinkedHashMap<>();
            for (OrderLine line : order.lines()) {
                List<BundleComponent> components = store.listComponentsOfBundle(line.productId());
                if (components.isEmpty()) {
                    needed.merge(line.productId(), line.quantity(), Integer::sum);
                }
                for (BundleComponent component : components) {
                    needed.merge(component.component().id(), component.quantity() * line.quantity(), Integer::sum);
                }
            }
            boolean shippable = true;
            for (Map.Entry<Long, Integer> entry : needed.entrySet()) {
                if (store.findStockQuantityInWarehouse(warehouse.id(), entry.getKey()) < entry.getValue()) {
                    shippable = false;
                }
            }
            if (shippable) {
                result.add(backOrder);
            }
        }
        return result;
    }

    /** Per product category, the waiting back orders with a line in it and the units on those lines, by category. */
    public List<CategoryBackOrders> listPerCategory() {
        return store.countWaitingBackOrdersPerCategory();
    }
}
