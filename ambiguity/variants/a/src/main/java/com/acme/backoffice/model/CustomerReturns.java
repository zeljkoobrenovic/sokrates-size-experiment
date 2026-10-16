package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.util.List;

/** The returns of a customer in a period. */
public record CustomerReturns(long customerId, String customerName, List<CustomerReturn> returns) {
    /** The value of all the returns. */
    public BigDecimal returnedValue() {
        return returns.stream().map(CustomerReturn::returnedValue).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
