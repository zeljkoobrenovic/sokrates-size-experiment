package com.acme.backoffice.model;

import java.time.DayOfWeek;
import java.util.Map;

/** Shipments sent on one day of the week: the total and the number per region (every region present). */
public record WeekdayShipments(DayOfWeek weekday, int total, Map<Region, Integer> perRegion) {
}
