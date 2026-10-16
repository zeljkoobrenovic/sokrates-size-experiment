package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CustomerPrice;
import com.acme.backoffice.model.PriceListUsage;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Promotion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Customer price lists: per product a special unit price for a period. Periods of one product must not overlap. */
public class PriceListService {
    private final DataStore store;

    public PriceListService(DataStore store) {
        this.store = store;
    }

    public CustomerPrice addPrice(long customerId, long productId, BigDecimal unitPrice, LocalDate startDate, LocalDate endDate) {
        if (unitPrice == null || unitPrice.signum() < 0) {
            throw new IllegalArgumentException("The price must not be negative");
        }
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("The end date must not be before the start date");
        }
        if (store.findCustomerById(customerId).isEmpty()) {
            throw new IllegalArgumentException("Unknown customer: " + customerId);
        }
        if (store.findProductById(productId).isEmpty()) {
            throw new IllegalArgumentException("Unknown product: " + productId);
        }
        if (!store.listOverlappingCustomerPrices(customerId, productId, startDate, endDate).isEmpty()) {
            throw new IllegalStateException("The customer already has a price for this product in that period");
        }
        return store.createCustomerPrice(new CustomerPrice(0, customerId, productId, unitPrice, startDate, endDate));
    }

    public void removePrice(long priceId) {
        if (store.findCustomerPriceById(priceId).isEmpty()) {
            throw new IllegalArgumentException("Unknown customer price: " + priceId);
        }
        store.deleteCustomerPrice(priceId);
    }

    public List<CustomerPrice> listPrices(long customerId) {
        return store.listCustomerPricesForActiveProducts(customerId);
    }

    /** The customer's special prices valid on the date for the products of the category (including its subcategories). */
    public List<CustomerPrice> listPricesByCategory(long customerId, String category, LocalDate date) {
        if (category == null || category.isBlank() || date == null) {
            throw new IllegalArgumentException("The category and the date are required");
        }
        return store.listCustomerPricesValidOnDateInCategoryTree(customerId, category, date);
    }

    /** The special prices of all customers that expire in the given number of days after the date, ordered per customer. */
    public List<CustomerPrice> listExpiringPrices(LocalDate date, int days) {
        if (date == null || days < 1) {
            throw new IllegalArgumentException("The date and a positive number of days are required");
        }
        return store.listCustomerPricesExpiringWithinDaysAfter(date, days);
    }

    /** Per customer the lines sold at a special price in the period (inclusive), with the revenue at special and catalogue prices. */
    public List<PriceListUsage> usage(LocalDate from, LocalDate to) {
        return store.sumCustomerPriceUsagePerCustomerBetween(from, to);
    }

    /** The unit price the customer pays on the date: the special price, else the catalogue price less the best promotion. */
    public BigDecimal priceFor(long customerId, long productId, LocalDate date) {
        Optional<CustomerPrice> special = store.findCustomerPriceValidOnDate(customerId, productId, date);
        if (special.isPresent()) {
            return special.get().unitPrice();
        }
        Product product = store.findProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + productId));
        Optional<Promotion> best = store.findHighestDiscountPromotionForCategoryOnDate(product.category(), date);
        if (best.isEmpty()) {
            return product.unitPrice();
        }
        BigDecimal factor = new BigDecimal("100").subtract(best.get().discountPercent());
        return product.unitPrice().multiply(factor).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
}
