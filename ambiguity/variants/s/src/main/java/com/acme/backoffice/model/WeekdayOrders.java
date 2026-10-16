package com.acme.backoffice.model;

import java.time.DayOfWeek;
import java.util.Map;

/** Orders placed on one day of the week: the total and the number per region (every region present). */
public record WeekdayOrders(DayOfWeek weekday, int total, Map<Region, Integer> perRegion) {
}
