package com.acme.backoffice.model;

/** Number of active customers, customers on hold, new customers in a period and dormant customers of a region. */
public record RegionCustomers(Region region, int activeCount, int onHoldCount, int newCount, int dormantCount) {
}
