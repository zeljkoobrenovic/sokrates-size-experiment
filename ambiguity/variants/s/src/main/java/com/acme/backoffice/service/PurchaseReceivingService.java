package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Category;
import com.acme.backoffice.model.CategoryPurchaseOrders;
import com.acme.backoffice.model.LatePurchaseOrder;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ProductPurchaseOrders;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.SupplierPerformance;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseLatePurchaseOrders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Receiving of purchase orders: receiving adds the ordered quantities to the stock of one warehouse. */
public class PurchaseReceivingService {
    private final DataStore store;

    public PurchaseReceivingService(DataStore store) {
        this.store = store;
    }

    /**
     * Receives a sent purchase order into the warehouse on the date and marks it as received. Fails with IllegalStateException,
     * changing nothing, if the purchase order is not sent.
     */
    public void receive(String orderNumber, String warehouseCode, LocalDate receivedDate) {
        if (receivedDate == null) {
            throw new IllegalArgumentException("Receiving date is required");
        }
        PurchaseOrder order = find(orderNumber);
        Warehouse warehouse = store.findWarehouseByCode(warehouseCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown warehouse: " + warehouseCode));
        if (!store.receivePurchaseOrder(order.id(), warehouse.id(), receivedDate)) {
            throw new IllegalStateException("Purchase order " + orderNumber + " cannot be received, it is " + order.status());
        }
    }

    public Optional<LocalDate> findReceivedDate(String orderNumber) {
        return store.findPurchaseOrderReceivedDate(find(orderNumber).id());
    }

    /**
     * Per supplier, the number of purchase orders received between the two dates (inclusive), the average delay in days against
     * the expected date (negative if early) and the percentage received late. Sorted by supplier name.
     */
    public List<SupplierPerformance> getSupplierPerformance(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listSupplierPerformance(from, to);
    }

    /**
     * The purchase orders containing the product, ordered between the two dates (inclusive), and the purchased quantity
     * of the product per supplier. Fails with IllegalArgumentException for an unknown product or invalid dates.
     */
    public ProductPurchaseOrders listPurchaseOrdersByProduct(String sku, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        Product product = store.findProductBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + sku));
        return new ProductPurchaseOrders(product, store.listPurchaseOrdersByProductAndPeriod(product.id(), from, to),
                store.findPurchasedQuantityPerSupplier(product.id(), from, to));
    }

    /**
     * The non-cancelled purchase orders containing products of the category or its subcategories, ordered between the
     * two dates (inclusive), and the spend per category. Fails with IllegalArgumentException for an unknown category or
     * invalid dates.
     */
    public CategoryPurchaseOrders listPurchaseOrdersByCategory(String code, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        Category category = store.findCategoryByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Unknown category: " + code));
        return new CategoryPurchaseOrders(category, store.listPurchaseOrdersByCategoryAndPeriod(code, from, to),
                store.findPurchaseSpendPerCategory(code, from, to));
    }

    /**
     * The purchase orders received into the warehouse between the two dates (inclusive) after their expected date, with the
     * days late. Fails with IllegalArgumentException for an unknown warehouse or invalid dates.
     */
    public WarehouseLatePurchaseOrders listLateByWarehouse(String warehouseCode, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        Warehouse warehouse = store.findWarehouseByCode(warehouseCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown warehouse: " + warehouseCode));
        return new WarehouseLatePurchaseOrders(warehouse, store.listLatePurchaseOrdersByWarehouseAndPeriod(warehouse.id(), from, to));
    }

    private PurchaseOrder find(String number) {
        return store.findPurchaseOrderByNumber(number)
                .orElseThrow(() -> new IllegalArgumentException("Unknown purchase order: " + number));
    }
}
