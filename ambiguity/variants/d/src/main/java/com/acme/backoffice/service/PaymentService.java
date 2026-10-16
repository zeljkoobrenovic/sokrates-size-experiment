package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CustomerUnappliedPayments;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionPayments;
import com.acme.backoffice.model.UnappliedPayment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Payments of invoices: an invoice is paid as soon as its payments reach the invoice amount. */
public class PaymentService {
    private final DataStore store;

    public PaymentService(DataStore store) {
        this.store = store;
    }

    /** Only an open invoice accepts payments, and a payment may not exceed the amount still due. */
    public Payment pay(String invoiceNumber, LocalDate paymentDate, BigDecimal amount) {
        if (paymentDate == null) {
            throw new IllegalArgumentException("Payment date is required");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        Invoice invoice = store.findInvoiceByNumber(invoiceNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unknown invoice: " + invoiceNumber));
        if (invoice.status() != InvoiceStatus.OPEN) {
            throw new IllegalStateException("Invoice " + invoiceNumber + " cannot be paid, it is " + invoice.status());
        }
        BigDecimal due = invoice.amount().subtract(store.sumPaymentsOfInvoice(invoice.id()));
        if (amount.compareTo(due) > 0) {
            throw new IllegalStateException("Payment " + amount + " exceeds the amount due " + due
                    + " of invoice " + invoiceNumber);
        }
        Payment payment = store.createPayment(new Payment(0, invoice.id(), paymentDate, amount));
        if (amount.compareTo(due) == 0) {
            store.updateInvoiceStatus(invoice.id(), InvoiceStatus.PAID);
        }
        return payment;
    }

    public List<Payment> listByInvoice(String invoiceNumber) {
        Invoice invoice = store.findInvoiceByNumber(invoiceNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unknown invoice: " + invoiceNumber));
        return store.findPaymentsOfInvoice(invoice.id());
    }

    /** The payments received from customers of the region between the two dates, inclusive, with their totals per month. */
    public RegionPayments listByRegion(Region region, LocalDate from, LocalDate to) {
        if (region == null || from == null || to == null) {
            throw new IllegalArgumentException("Region and dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before start date");
        }
        return new RegionPayments(region, store.findPaymentsReceivedInRegionBetween(region, from, to),
                store.findPaymentTotalsPerMonthByRegion(region, from, to));
    }

    /**
     * The payments that exceed the amount due of their invoice, grouped per customer (by name) with the unapplied
     * amount of each payment. Customers without overpayments are left out.
     */
    public List<CustomerUnappliedPayments> listUnapplied() {
        List<CustomerUnappliedPayments> result = new ArrayList<>();
        List<UnappliedPayment> current = null;
        long currentCustomerId = -1;
        for (UnappliedPayment payment : store.findUnappliedPayments()) {
            if (current == null || payment.customerId() != currentCustomerId) {
                current = new ArrayList<>();
                currentCustomerId = payment.customerId();
                result.add(new CustomerUnappliedPayments(payment.customerId(), payment.customerName(), current));
            }
            current.add(payment);
        }
        return result;
    }

    public BigDecimal amountDue(String invoiceNumber) {
        Invoice invoice = store.findInvoiceByNumber(invoiceNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unknown invoice: " + invoiceNumber));
        return invoice.amount().subtract(store.sumPaymentsOfInvoice(invoice.id()));
    }
}
