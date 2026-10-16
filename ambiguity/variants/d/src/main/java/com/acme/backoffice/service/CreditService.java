package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CustomerOverLimit;
import com.acme.backoffice.model.RegionOverLimit;
import com.acme.backoffice.model.RepOverLimit;
import com.acme.backoffice.model.SalesOrder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Credit limits of customers: the open invoice amount plus a new order may not exceed the limit. */
public class CreditService {
    private final DataStore store;

    public CreditService(DataStore store) {
        this.store = store;
    }

    /** Sets the credit limit of the customer; null removes the limit. */
    public void setCreditLimit(long customerId, BigDecimal limit) {
        if (limit != null && limit.signum() < 0) {
            throw new IllegalArgumentException("Credit limit may not be negative");
        }
        store.findCustomerById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown customer: " + customerId));
        store.updateCustomerCreditLimit(customerId, limit);
    }

    /** The limit minus the open invoice amount, negative if the limit is exceeded; empty if the customer has no limit. */
    public Optional<BigDecimal> availableCredit(long customerId) {
        Optional<BigDecimal> limit = store.findCustomerCreditLimit(customerId);
        if (limit.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(limit.get().subtract(store.sumOpenInvoiceBalanceOfCustomer(customerId)));
    }

    /** The customers whose open invoice amount on the date exceeds their credit limit, sorted by region and sales rep. */
    public List<CustomerOverLimit> getCustomersOverLimit(LocalDate asOf) {
        if (asOf == null) {
            throw new IllegalArgumentException("Date is required");
        }
        return store.findCustomersOverCreditLimit(asOf);
    }

    /** The number of customers over their credit limit on the date and their total excess per region, largest first. */
    public List<RegionOverLimit> getOverLimitPerRegion(LocalDate asOf) {
        if (asOf == null) {
            throw new IllegalArgumentException("Date is required");
        }
        return store.findOverCreditLimitPerRegion(asOf);
    }

    /** The number of customers over their credit limit on the date and their total excess per sales rep, largest first. */
    public List<RepOverLimit> getOverLimitPerSalesRep(LocalDate asOf) {
        if (asOf == null) {
            throw new IllegalArgumentException("Date is required");
        }
        return store.findOverCreditLimitPerSalesRep(asOf);
    }

    /** Throws if the order, added to the open invoice amount of its customer, would exceed the credit limit. */
    public void checkOrder(SalesOrder order) {
        Optional<BigDecimal> available = availableCredit(order.customerId());
        if (available.isEmpty()) {
            return;
        }
        BigDecimal value = store.sumSalesOrderValue(order.number());
        if (value.compareTo(available.get()) > 0) {
            throw new IllegalStateException("Order " + order.number() + " of " + value
                    + " exceeds the available credit " + available.get());
        }
    }
}
