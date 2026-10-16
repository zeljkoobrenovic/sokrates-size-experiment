package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CustomerPrice;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Promotion;
import com.acme.backoffice.model.SalesOrder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Places sales orders. For each line the best promotion (highest discount) active on the order date for the product's
 * category, or for all products, is applied to the given unit price and its code is recorded on the line. A valid special
 * price of the customer replaces the given unit price instead, and no promotion applies then.
 */
public class OrderEntryService {
    private final DataStore store;

    public OrderEntryService(DataStore store) {
        this.store = store;
    }

    /** Creates the order; the unit prices of the given lines are the list prices before any promotion. */
    public SalesOrder placeOrder(String number, long customerId, LocalDate orderDate, OrderStatus status, List<OrderLine> lines) {
        List<OrderLine> priced = new ArrayList<>();
        for (OrderLine line : lines) {
            Product product = store.findProductById(line.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + line.productId()));
            Optional<CustomerPrice> special = store.findValidCustomerPrice(customerId, line.productId(), orderDate);
            if (special.isPresent()) {
                priced.add(new OrderLine(line.productId(), line.quantity(), special.get().unitPrice(), null));
                continue;
            }
            Optional<Promotion> best = store.findBestPromotion(product.category(), orderDate);
            if (best.isEmpty()) {
                priced.add(new OrderLine(line.productId(), line.quantity(), line.unitPrice(), null));
            } else {
                BigDecimal factor = new BigDecimal("100").subtract(best.get().discountPercent());
                BigDecimal discounted = line.unitPrice().multiply(factor).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                priced.add(new OrderLine(line.productId(), line.quantity(), discounted, best.get().code()));
            }
        }
        return store.createSalesOrder(new SalesOrder(0, number, customerId, orderDate, status, priced));
    }
}
