package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CreditNote;
import com.acme.backoffice.model.CustomerOpenCredit;
import com.acme.backoffice.model.CustomerReturn;
import com.acme.backoffice.model.CustomerReturns;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.RegionOpenCredit;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.ReasonCredit;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseReturns;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Returns of sales orders: a return puts the returned quantities back into the stock of one warehouse. */
public class ReturnService {
    private final DataStore store;

    public ReturnService(DataStore store) {
        this.store = store;
    }

    /**
     * Registers a return for an order and adds the quantities to the warehouse stock. Only products of the order can be
     * returned, and in total not more than was ordered. Fails with IllegalArgumentException, changing nothing, otherwise.
     */
    public SalesReturn createReturn(String number, String orderNumber, LocalDate date, String reason, String warehouseCode,
                                    List<ReturnLine> lines) {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Return number is required");
        }
        if (date == null) {
            throw new IllegalArgumentException("Return date is required");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason is required");
        }
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("A return needs at least one line");
        }
        if (store.findSalesReturnByNumber(number).isPresent()) {
            throw new IllegalArgumentException("Return number already used: " + number);
        }
        SalesOrder order = store.findSalesOrderByNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unknown order: " + orderNumber));
        Warehouse warehouse = store.findWarehouseByCode(warehouseCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown warehouse: " + warehouseCode));
        Map<Long, Integer> remaining = new HashMap<>();
        for (OrderLine line : order.lines()) {
            remaining.merge(line.productId(), line.quantity(), Integer::sum);
        }
        for (SalesReturn earlier : store.listSalesReturnsOfOrder(order.id())) {
            for (ReturnLine line : earlier.lines()) {
                remaining.merge(line.productId(), -line.quantity(), Integer::sum);
            }
        }
        for (ReturnLine line : lines) {
            if (line.quantity() <= 0) {
                throw new IllegalArgumentException("Returned quantity must be positive");
            }
            if (!remaining.containsKey(line.productId())) {
                throw new IllegalArgumentException("Product " + line.productId() + " is not on order " + orderNumber);
            }
            int left = remaining.merge(line.productId(), -line.quantity(), Integer::sum);
            if (left < 0) {
                throw new IllegalArgumentException("Cannot return more of product " + line.productId() + " than was ordered");
            }
        }
        SalesReturn created = store.createSalesReturn(new SalesReturn(0, number, order.id(), date, reason, List.copyOf(lines)),
                warehouse.id());
        Map<Long, BigDecimal> prices = new HashMap<>();
        for (OrderLine line : order.lines()) {
            prices.putIfAbsent(line.productId(), line.unitPrice());
        }
        BigDecimal amount = BigDecimal.ZERO;
        for (ReturnLine line : lines) {
            amount = amount.add(prices.get(line.productId()).multiply(BigDecimal.valueOf(line.quantity())));
        }
        store.createCreditNote(new CreditNote(0, "CN-" + number, order.customerId(), created.id(), date, amount));
        return created;
    }

    public List<CreditNote> listCreditNotes(long customerId) {
        return store.listCreditNotesOfCustomer(customerId);
    }

    public List<CreditNote> listCreditNotes(LocalDate from, LocalDate to) {
        return store.listCreditNotesDatedBetween(from, to);
    }

    public List<CreditNote> listCreditNotes(Region region, LocalDate from, LocalDate to) {
        return store.listCreditNotesInRegionDatedBetween(region, from, to);
    }

    public List<ReasonCredit> creditedPerReason(LocalDate from, LocalDate to) {
        return store.sumCreditNotesPerReturnReasonBetween(from, to);
    }

    /**
     * Uses the credit note against an invoice of the same customer. Fails with IllegalArgumentException, changing
     * nothing, if either does not exist, they belong to different customers, or the note was already used.
     */
    public void applyCreditNote(String creditNoteNumber, String invoiceNumber, LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        CreditNote note = store.findCreditNoteByNumber(creditNoteNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unknown credit note " + creditNoteNumber));
        Invoice invoice = store.findInvoiceByNumber(invoiceNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unknown invoice " + invoiceNumber));
        if (note.customerId() != invoice.customerId()) {
            throw new IllegalArgumentException("Credit note and invoice belong to different customers");
        }
        if (store.isCreditNoteApplied(note.id())) {
            throw new IllegalArgumentException("Credit note " + creditNoteNumber + " has already been used");
        }
        store.createCreditNoteApplication(note.id(), invoice.id(), date);
    }

    /** The customers with credit notes not yet used against an invoice, per region, largest open credit first. */
    public List<CustomerOpenCredit> getCustomersWithOpenCredit() {
        return store.listCustomersWithOpenCredit();
    }

    /** The total open credit per region, largest first. */
    public List<RegionOpenCredit> getOpenCreditPerRegion() {
        return store.sumOpenCreditPerRegion();
    }

    public BigDecimal totalCredited(long customerId) {
        return store.sumCreditNotesOfCustomer(customerId);
    }

    public Optional<SalesReturn> findReturn(String number) {
        return store.findSalesReturnByNumber(number);
    }

    public List<SalesReturn> listByOrder(String orderNumber) {
        SalesOrder order = store.findSalesOrderByNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unknown order: " + orderNumber));
        return store.listSalesReturnsOfOrder(order.id());
    }

    public List<SalesReturn> listByCustomer(long customerId) {
        return store.listSalesReturnsOfCustomer(customerId);
    }

    /**
     * The returns dated between the two dates, inclusive, grouped per customer with their value; the customers with the
     * most returns first. Customers without returns in the period are left out.
     */
    public List<CustomerReturns> listReturnsPerCustomer(LocalDate from, LocalDate to) {
        List<CustomerReturns> result = new ArrayList<>();
        List<CustomerReturn> current = null;
        long currentCustomerId = -1;
        for (CustomerReturn ret : store.listSalesReturnsWithValuePerCustomerBetween(from, to)) {
            if (current == null || ret.customerId() != currentCustomerId) {
                current = new ArrayList<>();
                currentCustomerId = ret.customerId();
                result.add(new CustomerReturns(ret.customerId(), ret.customerName(), current));
            }
            current.add(ret);
        }
        return result;
    }

    /**
     * The returns received into the warehouse, dated between the two dates (inclusive), and the returned quantity per
     * product. Fails with IllegalArgumentException for an unknown warehouse or invalid dates.
     */
    public WarehouseReturns listReturnsByWarehouse(String warehouseCode, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        Warehouse warehouse = store.findWarehouseByCode(warehouseCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown warehouse: " + warehouseCode));
        return new WarehouseReturns(warehouse, store.listSalesReturnsIntoWarehouseDatedBetween(warehouse.id(), from, to),
                store.sumReturnedQuantityPerProductIntoWarehouseBetween(warehouse.id(), from, to));
    }
}
