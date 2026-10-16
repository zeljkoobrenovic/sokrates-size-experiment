package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Orders and revenue of the non-cancelled orders of a month and of the same month of the previous year, for a region
 * (or for all regions when the region is null).
 */
public record YearOverYearSales(Region region, int orderCount, BigDecimal revenue, int previousOrderCount,
                                BigDecimal previousRevenue) {

    /** Revenue per order of the month, zero when there are no orders. */
    public BigDecimal averageOrderValue() {
        return average(revenue, orderCount);
    }

    /** Revenue per order of the same month of the previous year, zero when there were no orders. */
    public BigDecimal previousAverageOrderValue() {
        return average(previousRevenue, previousOrderCount);
    }

    /** Revenue change against the previous year in percent, or null when the previous revenue is zero. */
    public BigDecimal revenueChangePercent() {
        return changePercent(revenue, previousRevenue);
    }

    /** Order count change against the previous year in percent, or null when there were no previous orders. */
    public BigDecimal orderChangePercent() {
        return changePercent(BigDecimal.valueOf(orderCount), BigDecimal.valueOf(previousOrderCount));
    }

    /** Average order value change against the previous year in percent, or null when the previous value is zero. */
    public BigDecimal averageOrderValueChangePercent() {
        return changePercent(averageOrderValue(), previousAverageOrderValue());
    }

    private static BigDecimal average(BigDecimal total, int count) {
        if (count == 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }

    private static BigDecimal changePercent(BigDecimal current, BigDecimal previous) {
        if (previous.signum() == 0) {
            return null;
        }
        return current.subtract(previous).multiply(BigDecimal.valueOf(100)).divide(previous, 2, RoundingMode.HALF_UP);
    }
}
