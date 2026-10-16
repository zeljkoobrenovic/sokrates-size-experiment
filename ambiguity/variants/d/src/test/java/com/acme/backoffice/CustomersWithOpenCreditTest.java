package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerOpenCredit;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionOpenCredit;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.ReturnService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomersWithOpenCreditTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ReturnService returns = new ReturnService(store);
    private Product bolt;
    private int counter;

    private Customer customer(String name, Region region) {
        return store.createCustomer(new Customer(0, name, name + "@example.com", null, "X", region, CustomerStatus.ACTIVE));
    }

    private void creditNote(Customer customer, int quantity) {
        counter++;
        store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), LocalDate.of(2024, 1, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(bolt.id(), 50, new BigDecimal("2.00")))));
        returns.createReturn("R-" + counter, "SO-" + counter, LocalDate.of(2024, 3, 1), "Damaged", "WH-N",
                List.of(new ReturnLine(bolt.id(), quantity)));
    }

    private void invoice(Customer customer, String number, String orderNumber) {
        SalesOrder order = store.findSalesOrderByNumber(orderNumber).orElseThrow();
        store.createInvoice(new Invoice(0, number, order.id(), customer.id(), LocalDate.of(2024, 3, 5),
                LocalDate.of(2024, 4, 4), new BigDecimal("50.00"), InvoiceStatus.OPEN));
    }

    @BeforeEach
    void setUp() {
        store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        bolt = store.createProduct(new Product(0, "B-1", "Bolt", "Fasteners", new BigDecimal("2.00"), "piece", true));
        Customer anna = customer("Anna", Region.NORTH);
        Customer ben = customer("Ben", Region.NORTH);
        Customer cleo = customer("Cleo", Region.SOUTH);
        customer("Dan", Region.EAST);
        creditNote(anna, 5);   // CN-R-1: 10.00
        creditNote(anna, 3);   // CN-R-2: 6.00
        creditNote(ben, 10);   // CN-R-3: 20.00
        creditNote(cleo, 4);   // CN-R-4: 8.00
        creditNote(cleo, 1);   // CN-R-5: 2.00
        invoice(anna, "INV-A", "SO-1");
        invoice(ben, "INV-B", "SO-3");
        invoice(cleo, "INV-C", "SO-4");
    }

    private String describe(CustomerOpenCredit c) {
        return c.region() + ":" + c.name() + ":" + c.noteCount() + ":" + c.openAmount().toPlainString();
    }

    private String describe(RegionOpenCredit r) {
        return r.region() + ":" + r.customerCount() + ":" + r.noteCount() + ":" + r.openAmount().toPlainString();
    }

    @Test
    void everyCreditNoteIsOpenUntilUsed() {
        assertEquals(List.of("NORTH:Ben:1:20.00", "NORTH:Anna:2:16.00", "SOUTH:Cleo:2:10.00"),
                returns.getCustomersWithOpenCredit().stream().map(this::describe).toList());
        assertEquals(List.of("NORTH:2:3:36.00", "SOUTH:1:2:10.00"),
                returns.getOpenCreditPerRegion().stream().map(this::describe).toList());
    }

    @Test
    void usedCreditNotesAreNotOpenAnyMore() {
        returns.applyCreditNote("CN-R-1", "INV-A", LocalDate.of(2024, 3, 6));
        returns.applyCreditNote("CN-R-4", "INV-C", LocalDate.of(2024, 3, 6));
        returns.applyCreditNote("CN-R-5", "INV-C", LocalDate.of(2024, 3, 6));
        assertEquals(List.of("NORTH:Ben:1:20.00", "NORTH:Anna:1:6.00"),
                returns.getCustomersWithOpenCredit().stream().map(this::describe).toList());
        assertEquals(List.of("NORTH:2:2:26.00"), returns.getOpenCreditPerRegion().stream().map(this::describe).toList());
    }

    @Test
    void nothingIsOpenWhenAllNotesAreUsed() {
        LocalDate day = LocalDate.of(2024, 3, 6);
        returns.applyCreditNote("CN-R-1", "INV-A", day);
        returns.applyCreditNote("CN-R-2", "INV-A", day);
        returns.applyCreditNote("CN-R-3", "INV-B", day);
        returns.applyCreditNote("CN-R-4", "INV-C", day);
        returns.applyCreditNote("CN-R-5", "INV-C", day);
        assertTrue(returns.getCustomersWithOpenCredit().isEmpty());
        assertTrue(returns.getOpenCreditPerRegion().isEmpty());
    }

    @Test
    void rejectsInvalidApplications() {
        LocalDate day = LocalDate.of(2024, 3, 6);
        assertThrows(IllegalArgumentException.class, () -> returns.applyCreditNote("CN-NONE", "INV-A", day));
        assertThrows(IllegalArgumentException.class, () -> returns.applyCreditNote("CN-R-1", "INV-NONE", day));
        assertThrows(IllegalArgumentException.class, () -> returns.applyCreditNote("CN-R-1", "INV-C", day));
        assertThrows(IllegalArgumentException.class, () -> returns.applyCreditNote("CN-R-1", "INV-A", null));
        returns.applyCreditNote("CN-R-1", "INV-A", day);
        assertThrows(IllegalArgumentException.class, () -> returns.applyCreditNote("CN-R-1", "INV-A", day));
        assertEquals(3, returns.getCustomersWithOpenCredit().size());
    }
}
