package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CreditNote;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatement;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.PaymentReceived;
import com.acme.backoffice.model.StatementEntry;
import com.acme.backoffice.model.StatementEntryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Customer statements: invoices increase what the customer owes, payments and credit notes decrease it. Cancelled
 * invoices and their payments are left out. Entries on the same day are listed as invoices, payments, credit notes.
 */
public class StatementService {
    private final DataStore store;

    public StatementService(DataStore store) {
        this.store = store;
    }

    /** The statement of the customer for the period between the two dates, inclusive. */
    public CustomerStatement statement(long customerId, LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid statement period");
        }
        Customer customer = store.findCustomerById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown customer: " + customerId));
        BigDecimal opening = store.sumNonCancelledInvoiceAmountOfCustomerDatedBefore(customerId, from)
                .subtract(store.sumPaymentsOfCustomerOnNonCancelledInvoicesMadeBefore(customerId, from))
                .subtract(store.sumCreditNotesOfCustomerDatedBefore(customerId, from));

        List<StatementEntry> unsorted = new ArrayList<>();
        for (Invoice invoice : store.listNonCancelledInvoicesOfCustomerDatedBetween(customerId, from, to)) {
            unsorted.add(new StatementEntry(invoice.invoiceDate(), StatementEntryType.INVOICE, invoice.number(),
                    invoice.amount(), null));
        }
        for (PaymentReceived payment : store.listPaymentsOfCustomerOnNonCancelledInvoicesReceivedBetween(customerId, from, to)) {
            unsorted.add(new StatementEntry(payment.paymentDate(), StatementEntryType.PAYMENT, payment.invoiceNumber(),
                    payment.amount().negate(), null));
        }
        for (CreditNote note : store.listCreditNotesOfCustomerDatedBetween(customerId, from, to)) {
            unsorted.add(new StatementEntry(note.creditDate(), StatementEntryType.CREDIT_NOTE, note.number(),
                    note.amount().negate(), null));
        }
        unsorted.sort(Comparator.comparing(StatementEntry::date));

        List<StatementEntry> entries = new ArrayList<>();
        BigDecimal balance = opening;
        for (StatementEntry entry : unsorted) {
            balance = balance.add(entry.amount());
            entries.add(new StatementEntry(entry.date(), entry.type(), entry.reference(), entry.amount(), balance));
        }
        return new CustomerStatement(customer.id(), customer.name(), from, to, opening, entries, balance);
    }
}
