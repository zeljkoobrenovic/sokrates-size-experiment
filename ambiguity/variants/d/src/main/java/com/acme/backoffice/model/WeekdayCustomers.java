package com.acme.backoffice.model;

import java.time.DayOfWeek;
import java.util.Map;

/** Customers created on one day of the week: the total and the number per region (every region present). */
public record WeekdayCustomers(DayOfWeek weekday, int total, Map<Region, Integer> perRegion) {
}
