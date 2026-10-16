package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number and total value of the non-cancelled orders that came from a sales channel in a period. */
public record ChannelSales(SalesChannel channel, int orderCount, BigDecimal revenue) {
}
