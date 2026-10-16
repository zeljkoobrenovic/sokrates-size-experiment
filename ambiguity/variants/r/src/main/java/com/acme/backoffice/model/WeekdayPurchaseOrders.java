package com.acme.backoffice.model;

import java.time.DayOfWeek;
import java.util.Map;

/** Purchase orders placed on one day of the week: the total and the number per region (every region present). */
public record WeekdayPurchaseOrders(DayOfWeek weekday, int total, Map<Region, Integer> perRegion) {
}
