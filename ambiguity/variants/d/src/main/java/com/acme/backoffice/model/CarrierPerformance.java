package com.acme.backoffice.model;

import java.math.BigDecimal;

/**
 * Delivery performance of a carrier for the shipments of a period: the number of shipments, the average days from shipping
 * to delivery of the delivered ones (null if none is delivered) and the number delivered later than the promised days.
 */
public record CarrierPerformance(Carrier carrier, int shipmentCount, BigDecimal averageDaysToDeliver, int lateCount) {
}
