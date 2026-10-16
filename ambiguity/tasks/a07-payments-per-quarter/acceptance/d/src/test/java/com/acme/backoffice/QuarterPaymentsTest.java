package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The number of payments received per quarter of a year, overall and per region. The new query is found by what it
 * is (a public data-access query about payments per quarter that takes the year), not by its exact name, and each
 * row is read by its parts: the quarter number and the total count (in that order) and the counts per region.
 */
class QuarterPaymentsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
    private int counter;

    private Customer customer(Region region) {
        counter++;
        return store.createCustomer(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, "UK", region, CustomerStatus.ACTIVE));
    }

    private Invoice invoice(Customer customer) {
        counter++;
        LocalDate date = LocalDate.of(2023, 12, 1);
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), date, OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal("10.00")))));
        return store.createInvoice(new Invoice(0, "INV-" + counter, order.id(), customer.id(), date, date.plusDays(30),
                new BigDecimal("100.00"), InvoiceStatus.OPEN));
    }

    private void pay(Invoice invoice, LocalDate date) {
        store.createPayment(new Payment(0, invoice.id(), date, new BigDecimal("10.00")));
    }

    private static Method query() {
        List<Method> found = new ArrayList<>();
        for (Method method : DataStore.class.getMethods()) {
            String name = method.getName().toLowerCase();
            if (Modifier.isPublic(method.getModifiers()) && name.contains("payment") && name.contains("quarter")
                    && Arrays.equals(method.getParameterTypes(), new Class<?>[]{int.class})
                    && List.class.isAssignableFrom(method.getReturnType())) {
                found.add(method);
            }
        }
        assertEquals(1, found.size(), "expected one query for the payments per quarter of a year, found " + found);
        return found.get(0);
    }

    /** A row as [quarter, total, north, south, east, west]. */
    private static List<Long> row(Object row) {
        assertTrue(row.getClass().isRecord(), "a row should be a record");
        List<Long> numbers = new ArrayList<>();
        Map<?, ?> perRegion = null;
        try {
            for (RecordComponent component : row.getClass().getRecordComponents()) {
                Object value = component.getAccessor().invoke(row);
                if (value instanceof Number number) {
                    numbers.add(number.longValue());
                } else if (value instanceof Map<?, ?> map) {
                    perRegion = map;
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
        assertEquals(2, numbers.size(), "a row should hold the quarter and the total");
        assertNotNull(perRegion, "a row should hold the counts per region");
        Map<Region, Long> counts = new EnumMap<>(Region.class);
        perRegion.forEach((region, count) -> counts.put((Region) region, ((Number) count).longValue()));
        List<Long> result = new ArrayList<>(numbers);
        for (Region region : List.of(Region.NORTH, Region.SOUTH, Region.EAST, Region.WEST)) {
            assertTrue(counts.containsKey(region), "every region should be present, missing " + region);
            result.add(counts.get(region));
        }
        return result;
    }

    private List<List<Long>> run(int year) throws Exception {
        List<?> rows = (List<?>) query().invoke(store, year);
        return rows.stream().map(QuarterPaymentsTest::row).toList();
    }

    @Test
    void countsPaymentsPerQuarterOverallAndPerRegion() throws Exception {
        Invoice north = invoice(customer(Region.NORTH));
        Invoice south = invoice(customer(Region.SOUTH));
        Invoice east = invoice(customer(Region.EAST));
        Invoice west = invoice(customer(Region.WEST));
        Invoice cancelled = invoice(customer(Region.NORTH));
        pay(north, LocalDate.of(2024, 1, 1));
        pay(north, LocalDate.of(2024, 3, 31));
        pay(south, LocalDate.of(2024, 2, 15));
        pay(east, LocalDate.of(2024, 4, 1));
        pay(east, LocalDate.of(2024, 6, 30));
        pay(west, LocalDate.of(2024, 12, 31));
        pay(north, LocalDate.of(2023, 12, 31));
        pay(west, LocalDate.of(2025, 1, 1));
        pay(cancelled, LocalDate.of(2024, 1, 10));
        store.updateInvoiceStatus(cancelled.id(), InvoiceStatus.CANCELLED);

        assertEquals(List.of(
                List.of(1L, 3L, 2L, 1L, 0L, 0L),
                List.of(2L, 2L, 0L, 0L, 2L, 0L),
                List.of(3L, 0L, 0L, 0L, 0L, 0L),
                List.of(4L, 1L, 0L, 0L, 0L, 1L)), run(2024));
    }

    @Test
    void yearWithoutPaymentsHasFourEmptyQuarters() throws Exception {
        assertEquals(List.of(
                List.of(1L, 0L, 0L, 0L, 0L, 0L),
                List.of(2L, 0L, 0L, 0L, 0L, 0L),
                List.of(3L, 0L, 0L, 0L, 0L, 0L),
                List.of(4L, 0L, 0L, 0L, 0L, 0L)), run(2030));
    }
}
