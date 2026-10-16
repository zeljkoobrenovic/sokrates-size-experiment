package com.acme.backoffice.model;

import java.util.List;

/** The customers of one segment in one region, with their number. */
public record SegmentRegionCustomers(CustomerSegment segment, Region region, int customerCount, List<CustomerRegionSales> customers) {
}
