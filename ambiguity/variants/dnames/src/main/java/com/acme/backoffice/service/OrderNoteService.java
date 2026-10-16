package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.OrderNote;
import com.acme.backoffice.model.SalesOrder;

import java.time.LocalDate;
import java.util.List;

/** Notes that staff add to sales orders. */
public class OrderNoteService {
    private final DataStore store;

    public OrderNoteService(DataStore store) {
        this.store = store;
    }

    public OrderNote addNote(String orderNumber, String author, LocalDate date, String text) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("The author must not be empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("The date must not be empty");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("The text must not be empty");
        }
        SalesOrder order = store.findSalesOrderByNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unknown order: " + orderNumber));
        return store.createOrderNote(new OrderNote(0, order.id(), author, date, text));
    }

    /** The notes of the order, newest first. */
    public List<OrderNote> listNotes(String orderNumber) {
        SalesOrder order = store.findSalesOrderByNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unknown order: " + orderNumber));
        return store.listOrderNotesOfOrder(order.id());
    }

    /** The notes by the author written from the first date to the last date, both included, newest first. */
    public List<OrderNote> listNotesByAuthor(String author, LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("The end date must not be before the start date");
        }
        return store.listOrderNotesOfAuthorDatedBetween(author, from, to);
    }

    /** The notes of all orders of the customer, newest first. */
    public List<OrderNote> listNotesByCustomer(long customerId) {
        return store.listOrderNotesOfCustomer(customerId);
    }

    /** The notes by the author on all orders of the customer, newest first. */
    public List<OrderNote> listNotesByAuthorAndCustomer(String author, long customerId) {
        return store.listOrderNotesOfAuthorForCustomer(author, customerId);
    }
}
