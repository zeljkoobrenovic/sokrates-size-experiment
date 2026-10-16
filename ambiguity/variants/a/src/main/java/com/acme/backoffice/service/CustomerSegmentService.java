package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CustomerRegionSales;
import com.acme.backoffice.model.CustomerSales;
import com.acme.backoffice.model.CustomerSegment;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesRepCustomer;
import com.acme.backoffice.model.SalesRepCustomerReportRow;
import com.acme.backoffice.model.SegmentRegionCustomers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Classifies customers into segments by their revenue of the 365 days before a given date. */
public class CustomerSegmentService {
    private static final BigDecimal A_ABOVE = new BigDecimal("50000");
    private static final BigDecimal B_FROM = new BigDecimal("10000");

    private final DataStore store;

    public CustomerSegmentService(DataStore store) {
        this.store = store;
    }

    public static CustomerSegment segmentOf(BigDecimal revenue) {
        if (revenue.compareTo(A_ABOVE) > 0) {
            return CustomerSegment.A;
        }
        if (revenue.compareTo(B_FROM) >= 0) {
            return CustomerSegment.B;
        }
        return CustomerSegment.C;
    }

    /** The customers of the segment, highest revenue first; revenue counts orders from 365 days before to the day before. */
    public List<CustomerSales> listCustomers(CustomerSegment segment, LocalDate asOf) {
        return store.findRevenuePerCustomerIncludingIdle(asOf.minusDays(365), asOf.minusDays(1)).stream()
                .filter(sales -> segmentOf(sales.revenue()) == segment)
                .toList();
    }

    /**
     * The customers of each segment per region, with their number, using the same revenue as the segments. Only
     * combinations with customers are returned, sorted by segment and region; customers highest revenue first.
     */
    public List<SegmentRegionCustomers> listPerSegmentAndRegion(LocalDate asOf) {
        if (asOf == null) {
            throw new IllegalArgumentException("Date is required");
        }
        List<CustomerRegionSales> all = store.findRevenuePerCustomerWithRegionIncludingIdle(asOf.minusDays(365), asOf.minusDays(1));
        List<SegmentRegionCustomers> groups = new ArrayList<>();
        for (CustomerSegment segment : CustomerSegment.values()) {
            for (Region region : Region.values()) {
                List<CustomerRegionSales> customers = all.stream()
                        .filter(sales -> segmentOf(sales.revenue()) == segment && sales.region() == region)
                        .toList();
                if (!customers.isEmpty()) {
                    groups.add(new SegmentRegionCustomers(segment, region, customers.size(), customers));
                }
            }
        }
        return groups;
    }

    /**
     * The customers of the sales rep that are not closed, by name, with their segment (same revenue as the segments),
     * last order date (null if none) and unpaid amount of their open invoices.
     */
    public List<SalesRepCustomerReportRow> salesRepCustomerReport(long salesRepId, LocalDate asOf) {
        if (asOf == null) {
            throw new IllegalArgumentException("Date is required");
        }
        List<SalesRepCustomerReportRow> rows = new ArrayList<>();
        for (SalesRepCustomer c : store.findCustomerReportBySalesRep(salesRepId, asOf.minusDays(365), asOf.minusDays(1))) {
            rows.add(new SalesRepCustomerReportRow(c.customerId(), c.name(), segmentOf(c.revenue()),
                    c.lastOrderDate(), c.openInvoiceAmount()));
        }
        return rows;
    }

    public Map<CustomerSegment, Integer> countPerSegment(LocalDate asOf) {
        Map<CustomerSegment, Integer> counts = new EnumMap<>(CustomerSegment.class);
        for (CustomerSegment segment : CustomerSegment.values()) {
            counts.put(segment, 0);
        }
        for (CustomerSales sales : store.findRevenuePerCustomerIncludingIdle(asOf.minusDays(365), asOf.minusDays(1))) {
            counts.merge(segmentOf(sales.revenue()), 1, Integer::sum);
        }
        return counts;
    }
}
