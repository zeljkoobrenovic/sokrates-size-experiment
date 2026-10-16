package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.BundleComponent;
import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.CarrierLeadTime;
import com.acme.backoffice.model.CarrierPerformance;
import com.acme.backoffice.model.CarrierShippedWeight;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ProductShipments;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.WarehouseDeliveryCount;
import com.acme.backoffice.model.WarehouseLateShipments;
import com.acme.backoffice.model.WarehouseShippedWeight;
import com.acme.backoffice.model.RegionLeadTime;
import com.acme.backoffice.model.RegionShipments;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.RemainingLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Shipment;
import com.acme.backoffice.model.StockMovementReason;
import com.acme.backoffice.model.Warehouse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Shipments of confirmed orders: shipping takes the ordered quantities from the stock of one warehouse. */
public class ShipmentService {
    private final DataStore store;

    public ShipmentService(DataStore store) {
        this.store = store;
    }

    /**
     * Ships a confirmed order from the warehouse with a carrier from the carrier list and marks the order as shipped. Fails with IllegalStateException,
     * changing nothing, if the order is not confirmed or the warehouse stock is short.
     */
    public Shipment ship(String orderNumber, String warehouseCode, String carrierName, String trackingNumber, LocalDate shipDate) {
        if (carrierName == null || carrierName.isBlank()) {
            throw new IllegalArgumentException("Carrier is required");
        }
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("Tracking number is required");
        }
        if (shipDate == null) {
            throw new IllegalArgumentException("Ship date is required");
        }
        SalesOrder order = find(orderNumber);
        Warehouse warehouse = findWarehouse(warehouseCode);
        Carrier carrier = store.findCarrierByName(carrierName)
                .orElseThrow(() -> new IllegalArgumentException("Unknown carrier: " + carrierName));
        if (order.status() != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Order " + orderNumber + " cannot be shipped, it is " + order.status());
        }
        Map<Long, Integer> needed = new LinkedHashMap<>();
        for (OrderLine line : order.lines()) {
            List<BundleComponent> components = store.listBundleComponents(line.productId());
            if (components.isEmpty()) {
                needed.merge(line.productId(), line.quantity(), Integer::sum);
            }
            for (BundleComponent component : components) {
                needed.merge(component.component().id(), component.quantity() * line.quantity(), Integer::sum);
            }
        }
        for (Map.Entry<Long, Integer> entry : needed.entrySet()) {
            int available = store.findStockQuantity(warehouse.id(), entry.getKey());
            if (available < entry.getValue()) {
                throw new IllegalStateException("Not enough stock in " + warehouseCode + " for product " + entry.getKey()
                        + ": need " + entry.getValue() + ", only " + available + " available");
            }
        }
        for (Map.Entry<Long, Integer> entry : needed.entrySet()) {
            if (!store.removeStockQuantity(warehouse.id(), entry.getKey(), entry.getValue())) {
                throw new IllegalStateException("Not enough stock in " + warehouseCode + " for product " + entry.getKey());
            }
            store.createStockMovement(entry.getKey(), warehouse.id(), shipDate, -entry.getValue(), StockMovementReason.SHIPMENT);
        }
        new OrderService(store).ship(orderNumber, shipDate);
        return store.createShipment(new Shipment(0, order.id(), warehouse.id(), carrier.id(), trackingNumber, shipDate));
    }

    /**
     * Ships part of one order line (line numbers count from 1) from the warehouse. A confirmed order becomes partially shipped; when
     * the last remaining quantity is shipped the order becomes shipped and gets its shipment, with the data of this last part. Fails with
     * IllegalStateException, changing nothing, if the order is neither confirmed nor partially shipped, the quantity is more than what
     * remains on the line, or the warehouse stock is short.
     */
    public void shipPart(String orderNumber, int lineNo, int quantity, String warehouseCode, String carrierName, String trackingNumber,
                         LocalDate shipDate) {
        if (carrierName == null || carrierName.isBlank()) {
            throw new IllegalArgumentException("Carrier is required");
        }
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("Tracking number is required");
        }
        if (shipDate == null) {
            throw new IllegalArgumentException("Ship date is required");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        SalesOrder order = find(orderNumber);
        if (lineNo < 1 || lineNo > order.lines().size()) {
            throw new IllegalArgumentException("Unknown line " + lineNo + " of order " + orderNumber);
        }
        Warehouse warehouse = findWarehouse(warehouseCode);
        Carrier carrier = store.findCarrierByName(carrierName)
                .orElseThrow(() -> new IllegalArgumentException("Unknown carrier: " + carrierName));
        if (order.status() != OrderStatus.CONFIRMED && order.status() != OrderStatus.PARTIALLY_SHIPPED) {
            throw new IllegalStateException("Order " + orderNumber + " cannot be shipped, it is " + order.status());
        }
        List<RemainingLine> remaining = store.listRemainingLines(order.id());
        RemainingLine line = remaining.stream().filter(r -> r.lineNo() == lineNo).findFirst()
                .orElseThrow(() -> new IllegalStateException("Line " + lineNo + " of order " + orderNumber + " is already shipped"));
        if (quantity > line.remaining()) {
            throw new IllegalStateException("Line " + lineNo + " of order " + orderNumber + " has only " + line.remaining() + " left to ship");
        }
        List<BundleComponent> components = store.listBundleComponents(line.productId());
        if (components.isEmpty()) {
            if (!store.removeStockQuantity(warehouse.id(), line.productId(), quantity)) {
                throw new IllegalStateException("Not enough stock in " + warehouseCode + " for product " + line.productId());
            }
            store.createStockMovement(line.productId(), warehouse.id(), shipDate, -quantity, StockMovementReason.SHIPMENT);
        } else {
            for (BundleComponent component : components) {
                int available = store.findStockQuantity(warehouse.id(), component.component().id());
                if (available < component.quantity() * quantity) {
                    throw new IllegalStateException("Not enough stock in " + warehouseCode + " for product " + component.component().id());
                }
            }
            for (BundleComponent component : components) {
                int taken = component.quantity() * quantity;
                if (!store.removeStockQuantity(warehouse.id(), component.component().id(), taken)) {
                    throw new IllegalStateException("Not enough stock in " + warehouseCode + " for product " + component.component().id());
                }
                store.createStockMovement(component.component().id(), warehouse.id(), shipDate, -taken, StockMovementReason.SHIPMENT);
            }
        }
        store.createShipmentPart(order.id(), lineNo, warehouse.id(), carrier.id(), trackingNumber, shipDate, quantity);
        boolean complete = remaining.size() == 1 && quantity == line.remaining();
        store.changeSalesOrderStatus(order.id(), complete ? OrderStatus.SHIPPED : OrderStatus.PARTIALLY_SHIPPED, shipDate);
        if (complete) {
            store.createShipment(new Shipment(0, order.id(), warehouse.id(), carrier.id(), trackingNumber, shipDate));
        }
    }

    /** The lines of the order, with what is still to be shipped; fully shipped lines are left out. */
    public List<RemainingLine> getRemaining(String orderNumber) {
        return store.listRemainingLines(find(orderNumber).id());
    }

    public Optional<Shipment> findByOrder(String orderNumber) {
        return store.findShipmentByOrder(find(orderNumber).id());
    }

    /** The shipments that left the warehouse between the two dates, both included, oldest first. */
    public List<Shipment> listByWarehouse(String warehouseCode, LocalDate from, LocalDate to) {
        return store.listShipmentsByWarehouseAndPeriod(findWarehouse(warehouseCode).id(), from, to);
    }

    /**
     * Records the delivery date reported by the carrier. Fails with IllegalStateException if the order has no shipment, it is already
     * delivered or the date is before the ship date.
     */
    public void markDelivered(String orderNumber, LocalDate deliveryDate) {
        if (deliveryDate == null) {
            throw new IllegalArgumentException("Delivery date is required");
        }
        Shipment shipment = store.findShipmentByOrder(find(orderNumber).id())
                .orElseThrow(() -> new IllegalStateException("Order " + orderNumber + " has not been shipped"));
        if (shipment.deliveryDate() != null) {
            throw new IllegalStateException("Order " + orderNumber + " is already delivered");
        }
        if (deliveryDate.isBefore(shipment.shipDate())) {
            throw new IllegalStateException("Delivery date is before the ship date of order " + orderNumber);
        }
        store.updateShipmentDeliveryDate(shipment.orderId(), deliveryDate);
    }

    /** The shipments shipped and not yet delivered, oldest first. */
    public List<Shipment> listInTransit() {
        return store.listShipmentsInTransit();
    }

    /** The shipments of the carrier shipped and not yet delivered, oldest first. */
    public List<Shipment> listInTransitByCarrier(String carrierName) {
        Carrier carrier = store.findCarrierByName(carrierName)
                .orElseThrow(() -> new IllegalArgumentException("Unknown carrier: " + carrierName));
        return store.listShipmentsInTransitByCarrier(carrier.id());
    }

    /** The shipments still in transit on the given date that were shipped more than the given number of days before, oldest first. */
    public List<Shipment> listInTransitLongerThan(int days, LocalDate asOf) {
        if (days < 0) {
            throw new IllegalArgumentException("Days must not be negative");
        }
        if (asOf == null) {
            throw new IllegalArgumentException("Date is required");
        }
        return store.listShipmentsInTransitShippedBefore(asOf.minusDays(days));
    }

    /**
     * Per carrier, the number of shipments shipped between the two dates (inclusive), the average days to deliver the delivered
     * ones and the number delivered later than the promised days of the carrier. Sorted by carrier name.
     */
    public List<CarrierPerformance> getCarrierPerformance(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listCarrierPerformance(from, to);
    }

    /**
     * The shipments to customers of the region shipped between the two dates (inclusive): those still in transit and those
     * delivered later than the promised days of the carrier, oldest first.
     */
    public RegionShipments listByRegion(Region region, LocalDate from, LocalDate to) {
        if (region == null || from == null || to == null) {
            throw new IllegalArgumentException("Region and dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return new RegionShipments(region, store.listShipmentsInTransitByRegionAndPeriod(region, from, to),
                store.listLateShipmentsByRegionAndPeriod(region, from, to));
    }

    /**
     * The shipments containing the product (by SKU) shipped between the two dates (inclusive), oldest first, and the quantity of the
     * product shipped per warehouse.
     */
    public ProductShipments listByProduct(String sku, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        Product product = store.findProductBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + sku));
        return new ProductShipments(product, store.listShipmentsByProductAndPeriod(product.id(), from, to),
                store.listShippedQuantityByProductAndWarehouse(product.id(), from, to));
    }

    /**
     * Per warehouse with delivered shipments shipped between the two dates (inclusive): the shipments delivered later than the
     * promised days of the carrier, oldest first, and the percentage of delivered shipments that were late, ordered by warehouse code.
     */
    public List<WarehouseLateShipments> listLateByWarehouse(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        List<WarehouseLateShipments> result = new ArrayList<>();
        for (WarehouseDeliveryCount count : store.listDeliveryCountsByWarehouse(from, to)) {
            BigDecimal percent = BigDecimal.valueOf(count.late() * 100L)
                    .divide(BigDecimal.valueOf(count.delivered()), 1, RoundingMode.HALF_UP);
            result.add(new WarehouseLateShipments(count.warehouse(),
                    store.listLateShipmentsByWarehouseAndPeriod(count.warehouse().id(), from, to), count.delivered(), percent));
        }
        return result;
    }

    /** Sets the weight in kg of one unit of the product (by SKU). */
    public void setProductWeight(String sku, BigDecimal weightKg) {
        if (weightKg == null || weightKg.signum() < 0) {
            throw new IllegalArgumentException("Weight must not be negative");
        }
        Product product = store.findProductBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + sku));
        store.saveProductWeight(product.id(), weightKg);
    }

    /** The weight in kg shipped by each carrier between the two dates (inclusive), ordered by carrier name. */
    public List<CarrierShippedWeight> shippedWeightByCarrier(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listShippedWeightByCarrier(from, to);
    }

    /** The weight in kg shipped from each warehouse between the two dates (inclusive), ordered by warehouse code. */
    public List<WarehouseShippedWeight> shippedWeightByWarehouse(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listShippedWeightByWarehouse(from, to);
    }

    private SalesOrder find(String number) {
        return store.findSalesOrderByNumber(number)
                .orElseThrow(() -> new IllegalArgumentException("Unknown order: " + number));
    }

    /** Per region, the delivered orders placed in the period and their average days from order to delivery. */
    public List<RegionLeadTime> getLeadTimeByRegion(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listLeadTimeByRegion(from, to);
    }

    /** Per carrier, the delivered orders placed in the period and their average days from order to delivery. */
    public List<CarrierLeadTime> getLeadTimeByCarrier(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listLeadTimeByCarrier(from, to);
    }

    private Warehouse findWarehouse(String code) {
        return store.findWarehouseByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Unknown warehouse: " + code));
    }
}
