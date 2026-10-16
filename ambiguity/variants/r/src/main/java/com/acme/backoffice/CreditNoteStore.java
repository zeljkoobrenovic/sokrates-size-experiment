package com.acme.backoffice;

import com.acme.backoffice.model.CreditNote;
import com.acme.backoffice.model.ReasonCredit;
import com.acme.backoffice.model.Region;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Credit notes issued for sales returns: creating them, applying a credit note to an invoice (once), finding one by
 * number, the credit notes of a customer, of a period and of a region, the credit note totals of a customer and the
 * credited amount per return reason.
 */
public final class CreditNoteStore {
    private static final String SELECT_NOTE = "SELECT n.id AS id, n.number AS number, n.customer_id AS customer_id, "
            + "n.return_id AS return_id, n.credit_date AS credit_date, n.amount AS amount FROM credit_note n";
    private static final String IN_PERIOD = "n.credit_date >= ? AND n.credit_date <= ?";
    private static final String OLDEST_FIRST = " ORDER BY n.credit_date, n.id";

    private final Jdbc jdbc;

    CreditNoteStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    /** Creates the credit note; the note with its generated id. */
    public CreditNote create(CreditNote note) {
        long id = jdbc.insert("Cannot create credit note",
                "INSERT INTO credit_note (number, customer_id, return_id, credit_date, amount) VALUES (?, ?, ?, ?, ?)",
                note.number(), note.customerId(), note.returnId(), note.creditDate(), note.amount());
        return new CreditNote(id, note.number(), note.customerId(), note.returnId(), note.creditDate(), note.amount());
    }

    /** Records that the credit note has been used against the invoice. A credit note can be used only once. */
    public void applyToInvoice(long creditNoteId, long invoiceId, LocalDate appliedDate) {
        jdbc.update("Cannot apply credit note",
                "INSERT INTO credit_note_application (credit_note_id, invoice_id, applied_date) VALUES (?, ?, ?)",
                creditNoteId, invoiceId, appliedDate);
    }

    /** Whether the credit note has already been used against an invoice. */
    public boolean isApplied(long creditNoteId) {
        return jdbc.queryInt("Cannot check credit note",
                "SELECT COUNT(*) AS cnt FROM credit_note_application WHERE credit_note_id = ?", creditNoteId) > 0;
    }

    /** The credit note with the number, if it exists. */
    public Optional<CreditNote> findByNumber(String number) {
        return jdbc.first("Cannot find credit note", SELECT_NOTE + " WHERE n.number = ?", Rows::creditNote, number);
    }

    /** The credit notes of the customer, oldest first (then by id). */
    public List<CreditNote> listOfCustomer(long customerId) {
        return listNotes("Cannot list credit notes", " WHERE n.customer_id = ?", customerId);
    }

    /** The credit notes of the customer dated between the two dates, inclusive, oldest first (then by id). */
    public List<CreditNote> listOfCustomerInPeriod(long customerId, LocalDate from, LocalDate to) {
        return listNotes("Cannot list credit notes by customer and period", " WHERE n.customer_id = ? AND " + IN_PERIOD,
                customerId, from, to);
    }

    /** The credit notes dated between the two dates, inclusive, oldest first (then by id). */
    public List<CreditNote> listInPeriod(LocalDate from, LocalDate to) {
        return listNotes("Cannot list credit notes by period", " WHERE " + IN_PERIOD, from, to);
    }

    /** The credit notes of the customers of the region dated between the two dates, inclusive, oldest first (then by id). */
    public List<CreditNote> listInRegionInPeriod(Region region, LocalDate from, LocalDate to) {
        // region.name(): a null region fails with a NullPointerException, as before
        return listNotes("Cannot list credit notes by region and period",
                " JOIN customer c ON c.id = n.customer_id WHERE c.region = ? AND " + IN_PERIOD, region.name(), from, to);
    }

    /** The sum of the credit notes of the customer, zero if there are none. */
    public BigDecimal findTotalOfCustomer(long customerId) {
        return totalOfCustomer("Cannot find credit note total", "", customerId);
    }

    /** The sum of the credit notes of the customer dated before the date, zero if there are none. */
    public BigDecimal findTotalOfCustomerBefore(long customerId, LocalDate before) {
        return totalOfCustomer("Cannot find credit note total before date", " AND credit_date < ?", customerId, before);
    }

    /**
     * Number and total amount of the credit notes dated between the two dates, inclusive, per reason of the underlying
     * return. Sorted by amount, highest first (then by reason); reasons without credit notes are left out.
     */
    public List<ReasonCredit> creditedPerReason(LocalDate from, LocalDate to) {
        String sql = "SELECT r.reason AS reason, COUNT(*) AS note_count, SUM(n.amount) AS total "
                + "FROM credit_note n JOIN sales_return r ON r.id = n.return_id "
                + "WHERE " + IN_PERIOD + " GROUP BY r.reason ORDER BY total DESC, r.reason";
        return jdbc.list("Cannot find credited amount per reason", sql,
                rs -> new ReasonCredit(rs.getString("reason"), rs.getInt("note_count"), rs.getBigDecimal("total")),
                from, to);
    }

    /** The credit notes selected by the joins and conditions that follow {@code FROM credit_note n}, oldest first (then by id). */
    private List<CreditNote> listNotes(String error, String joinsAndWhere, Object... params) {
        return jdbc.list(error, SELECT_NOTE + joinsAndWhere + OLDEST_FIRST, Rows::creditNote, params);
    }

    /** The sum of the credit notes of the customer meeting the further condition (starting with " AND "); zero if none. */
    private BigDecimal totalOfCustomer(String error, String condition, Object... params) {
        return jdbc.queryAmount(error,
                "SELECT COALESCE(SUM(amount), 0) AS total FROM credit_note WHERE customer_id = ?" + condition, params);
    }
}
