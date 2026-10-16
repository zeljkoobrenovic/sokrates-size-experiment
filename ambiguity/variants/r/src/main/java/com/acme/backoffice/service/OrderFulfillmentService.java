package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.BundleComponent;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseOrderFulfillment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Which open orders each warehouse could ship in full. An order is shippable from a warehouse when its own stock covers
 * all lines at once (bundles count by their components, as when shipping). Each order is judged on its own: stock is
 * not reserved between orders.
 */
public class OrderFulfillmentService {
    private final DataStore store;

    public OrderFulfillmentService(DataStore store) {
        this.store = store;
    }

    /** One entry per warehouse, ordered by code; orders are the open ones, oldest first. */
    public List<WarehouseOrderFulfillment> listOpenOrdersPerWarehouse() {
        List<SalesOrder> openOrders = store.orders().listOpen();
        List<WarehouseOrderFulfillment> result = new ArrayList<>();
        for (Warehouse warehouse : store.warehouses().listByCode()) {
            List<SalesOrder> shippable = new ArrayList<>();
            List<SalesOrder> notShippable = new ArrayList<>();
            for (SalesOrder order : openOrders) {
                if (canShip(warehouse, order)) {
                    shippable.add(order);
                } else {
                    notShippable.add(order);
                }
            }
            result.add(new WarehouseOrderFulfillment(warehouse, shippable, notShippable));
        }
        return result;
    }

    private boolean canShip(Warehouse warehouse, SalesOrder order) {
        Map<Long, Integer> needed = new LinkedHashMap<>();
        for (OrderLine line : order.lines()) {
            List<BundleComponent> components = store.bundles().listComponents(line.productId());
            if (components.isEmpty()) {
                needed.merge(line.productId(), line.quantity(), Integer::sum);
            }
            for (BundleComponent component : components) {
                needed.merge(component.component().id(), component.quantity() * line.quantity(), Integer::sum);
            }
        }
        for (Map.Entry<Long, Integer> entry : needed.entrySet()) {
            if (store.stock().findQuantity(warehouse.id(), entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }
}
