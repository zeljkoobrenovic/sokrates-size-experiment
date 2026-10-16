package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Currency;
import com.acme.backoffice.model.CurrencyInvoices;
import com.acme.backoffice.model.CurrencyOpenInvoice;
import com.acme.backoffice.model.CustomerCurrencyInvoices;
import com.acme.backoffice.model.CustomerLateInvoices;
import com.acme.backoffice.model.CustomerPartiallyPaidInvoices;
import com.acme.backoffice.model.PartiallyPaidInvoice;
import com.acme.backoffice.model.RegionPartiallyPaidInvoices;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.LatePaidInvoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.InvoiceStatusTotal;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionInvoices;
import com.acme.backoffice.model.RegionLargeInvoice;
import com.acme.backoffice.model.RegionLargeInvoices;
import com.acme.backoffice.model.SalesOrder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Invoices shipped orders: the amount is the order total and the invoice is due 30 days after the invoice date. */
public class InvoiceService {
    private static final int PAYMENT_TERM_DAYS = 30;

    private final DataStore store;

    public InvoiceService(DataStore store) {
        this.store = store;
    }

    /** Only a shipped (or already delivered) order can be invoiced, and only once unless its invoice was cancelled. */
    public Invoice invoice(String orderNumber, LocalDate invoiceDate) {
        if (invoiceDate == null) {
            throw new IllegalArgumentException("Invoice date is required");
        }
        SalesOrder order = store.findSalesOrderByNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unknown order: " + orderNumber));
        if (order.status() != OrderStatus.SHIPPED && order.status() != OrderStatus.DELIVERED) {
            throw new IllegalStateException("Order " + orderNumber + " cannot be invoiced, it is " + order.status());
        }
        if (store.findNonCancelledInvoiceOfOrder(order.id()).isPresent()) {
            throw new IllegalStateException("Order " + orderNumber + " is already invoiced");
        }
        String number = String.format("INV-%04d", store.countAllInvoices() + 1);
        return store.createInvoice(new Invoice(0, number, order.id(), order.customerId(), invoiceDate,
                invoiceDate.plusDays(PAYMENT_TERM_DAYS), store.sumSalesOrderValueByNumber(orderNumber), InvoiceStatus.OPEN));
    }

    public Optional<Invoice> find(String invoiceNumber) {
        return store.findInvoiceByNumber(invoiceNumber);
    }

    public List<Invoice> listByCustomer(long customerId) {
        return store.listAllInvoicesOfCustomerNewestFirst(customerId);
    }

    /** Open invoices whose due date is before the given date, oldest due date first. */
    public List<Invoice> listOverdue(LocalDate asOf) {
        if (asOf == null) {
            throw new IllegalArgumentException("Date is required");
        }
        return store.listOverdueInvoices(asOf);
    }

    public List<Invoice> listOverdueByCustomer(LocalDate asOf, long customerId) {
        if (asOf == null) {
            throw new IllegalArgumentException("Date is required");
        }
        return store.listOverdueInvoicesOfCustomer(asOf, customerId);
    }

    public List<Invoice> listOverdueByRegion(LocalDate asOf, Region region) {
        if (asOf == null || region == null) {
            throw new IllegalArgumentException("Date and region are required");
        }
        return store.listOverdueInvoicesInRegion(asOf, region);
    }

    /**
     * The open and paid invoices dated between the two dates, inclusive, of the customers of the region, with the
     * number and total per status. Both statuses are always in the totals, with 0 when there are no invoices.
     */
    public RegionInvoices listByRegionAndPeriod(Region region, LocalDate from, LocalDate to) {
        if (region == null || from == null || to == null) {
            throw new IllegalArgumentException("Region and dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before start date");
        }
        List<InvoiceStatusTotal> found = store.sumOpenAndPaidInvoicesPerStatusInRegionDatedBetween(region, from, to);
        List<InvoiceStatusTotal> totals = new ArrayList<>();
        for (InvoiceStatus status : List.of(InvoiceStatus.OPEN, InvoiceStatus.PAID)) {
            totals.add(found.stream().filter(total -> total.status() == status).findFirst()
                    .orElse(new InvoiceStatusTotal(status, 0, BigDecimal.ZERO)));
        }
        return new RegionInvoices(region, store.listOpenAndPaidInvoicesInRegionDatedBetween(region, from, to), totals);
    }

    /**
     * The invoices whose last payment came after the due date and between the two dates, inclusive, grouped per
     * customer (by name) with the days late of each invoice. Customers without late payments are left out.
     */
    public List<CustomerLateInvoices> listPaidLateByPeriod(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before start date");
        }
        List<CustomerLateInvoices> result = new ArrayList<>();
        List<LatePaidInvoice> current = null;
        long currentCustomerId = -1;
        for (LatePaidInvoice invoice : store.listLatePaidInvoicesLastPaidBetween(from, to)) {
            if (current == null || invoice.customerId() != currentCustomerId) {
                current = new ArrayList<>();
                currentCustomerId = invoice.customerId();
                result.add(new CustomerLateInvoices(invoice.customerId(), invoice.customerName(), current));
            }
            current.add(invoice);
        }
        return result;
    }

    /**
     * The open invoices with at least one payment, grouped per region and, within it, per customer (by name), with
     * the amount still due of each invoice, customer and region. Regions and customers without such invoices are left out.
     */
    public List<RegionPartiallyPaidInvoices> listPartiallyPaid() {
        List<RegionPartiallyPaidInvoices> result = new ArrayList<>();
        List<CustomerPartiallyPaidInvoices> customers = null;
        List<PartiallyPaidInvoice> current = null;
        Region currentRegion = null;
        long currentCustomerId = -1;
        for (PartiallyPaidInvoice invoice : store.listPartiallyPaidOpenInvoices()) {
            if (customers == null || invoice.region() != currentRegion) {
                customers = new ArrayList<>();
                currentRegion = invoice.region();
                currentCustomerId = -1;
                result.add(new RegionPartiallyPaidInvoices(currentRegion, customers));
            }
            if (invoice.customerId() != currentCustomerId) {
                current = new ArrayList<>();
                currentCustomerId = invoice.customerId();
                customers.add(new CustomerPartiallyPaidInvoices(invoice.customerId(), invoice.customerName(), current));
            }
            current.add(invoice);
        }
        return result;
    }

    /**
     * The open invoices grouped per currency (invoices without a recorded currency are in EUR) and, within it, per
     * customer (by name), with the amount of each invoice in its currency and in EUR. Currencies without open invoices are left out.
     */
    public List<CurrencyInvoices> listOpenByCurrency() {
        List<CurrencyInvoices> result = new ArrayList<>();
        List<CustomerCurrencyInvoices> customers = null;
        List<CurrencyOpenInvoice> current = null;
        Currency currentCurrency = null;
        long currentCustomerId = -1;
        for (CurrencyOpenInvoice invoice : store.listOpenInvoicesPerCurrency()) {
            if (customers == null || invoice.currency() != currentCurrency) {
                customers = new ArrayList<>();
                currentCurrency = invoice.currency();
                currentCustomerId = -1;
                result.add(new CurrencyInvoices(currentCurrency, customers));
            }
            if (invoice.customerId() != currentCustomerId) {
                current = new ArrayList<>();
                currentCustomerId = invoice.customerId();
                customers.add(new CustomerCurrencyInvoices(invoice.customerId(), invoice.customerName(), current));
            }
            current.add(invoice);
        }
        return result;
    }

    /**
     * The open and paid invoices dated between the two dates, inclusive, with an amount above the given amount, per
     * region of the customer (regions without such invoices are left out), biggest invoice first.
     */
    public List<RegionLargeInvoices> listLargeByRegion(BigDecimal minAmount, LocalDate from, LocalDate to) {
        if (minAmount == null || from == null || to == null) {
            throw new IllegalArgumentException("Amount and dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before start date");
        }
        List<RegionLargeInvoices> result = new ArrayList<>();
        List<Invoice> current = null;
        Region currentRegion = null;
        for (RegionLargeInvoice large : store.listOpenAndPaidInvoicesAboveAmountDatedBetweenPerRegion(minAmount, from, to)) {
            if (large.region() != currentRegion) {
                currentRegion = large.region();
                current = new ArrayList<>();
                result.add(new RegionLargeInvoices(currentRegion, current));
            }
            current.add(large.invoice());
        }
        return result;
    }
}
