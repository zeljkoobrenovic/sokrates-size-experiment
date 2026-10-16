package com.acme.backoffice.model;

import java.util.Map;

/** Sales returns dated in one calendar quarter (1 to 4): the total and the number per region (every region present). */
public record QuarterReturns(int quarter, int total, Map<Region, Integer> perRegion) {
}
