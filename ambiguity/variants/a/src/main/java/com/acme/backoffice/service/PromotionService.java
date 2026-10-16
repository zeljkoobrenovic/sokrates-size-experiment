package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CustomerPromotionLine;
import com.acme.backoffice.model.CustomerPromotions;
import com.acme.backoffice.model.Promotion;
import com.acme.backoffice.model.PromotionUsage;
import com.acme.backoffice.model.Region;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Promotions: a percentage discount for a period, for one product category or for all products. */
public class PromotionService {
    private final DataStore store;

    public PromotionService(DataStore store) {
        this.store = store;
    }

    /** Creates a promotion. A null or blank category means all products. */
    public Promotion createPromotion(String code, BigDecimal discountPercent, LocalDate startDate, LocalDate endDate, String category) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Promotion code is required");
        }
        if (discountPercent == null || discountPercent.signum() <= 0 || discountPercent.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Discount must be above 0 and at most 100 percent");
        }
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("The end date must not be before the start date");
        }
        if (store.findPromotionByCode(code).isPresent()) {
            throw new IllegalArgumentException("Duplicate promotion code: " + code);
        }
        String appliesTo = category == null || category.isBlank() ? null : category;
        return store.createPromotion(new Promotion(0, code, discountPercent, startDate, endDate, appliesTo));
    }

    /** The promotions whose period includes the date. */
    public List<Promotion> listActive(LocalDate date) {
        return store.listPromotionsActiveOn(date);
    }

    /** Limits the promotion to a region; without a limit a promotion applies in all regions. */
    public void limitToRegion(String code, Region region) {
        if (region == null) {
            throw new IllegalArgumentException("Region is required");
        }
        Promotion promotion = store.findPromotionByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Unknown promotion code: " + code));
        store.savePromotionRegion(promotion.id(), region);
    }

    /** The promotions whose period includes the date and that are limited to the region or not limited to any region. */
    public List<Promotion> listActive(LocalDate date, Region region) {
        if (region == null) {
            throw new IllegalArgumentException("Region is required");
        }
        return store.listPromotionsActiveOnInRegion(date, region);
    }

    /** The promotions that apply to exactly this category; promotions for all products are not included. */
    public List<Promotion> listByCategory(String category) {
        return store.listPromotionsByCategory(category);
    }

    /** Per promotion applied to order lines dated in the period (inclusive): lines, discount given and revenue, by code. */
    public List<PromotionUsage> usage(LocalDate from, LocalDate to) {
        return store.findPromotionUsage(from, to);
    }

    /** The promotions running in the period that were not applied to any order line in it, by code. */
    public List<Promotion> listUnused(LocalDate from, LocalDate to) {
        return store.listUnusedPromotions(from, to);
    }

    /** The lines of the customer's orders dated in the period (inclusive) that got a promotion, and the discount given to the customer. */
    public CustomerPromotions promotionsOfCustomer(long customerId, LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("The end date must not be before the start date");
        }
        List<CustomerPromotionLine> lines = store.findCustomerPromotionLines(customerId, from, to);
        BigDecimal discount = BigDecimal.ZERO;
        for (CustomerPromotionLine line : lines) {
            discount = discount.add(line.discount());
        }
        return new CustomerPromotions(lines, discount);
    }
}
