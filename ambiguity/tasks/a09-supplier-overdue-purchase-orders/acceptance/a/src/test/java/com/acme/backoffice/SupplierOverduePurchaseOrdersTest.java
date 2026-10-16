package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Supplier;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The overdue purchase orders of one supplier on a date. The new query is found by what it is (a public data-access
 * query about overdue purchase orders of a supplier that takes the supplier's id and the date, in either order), not
 * by its exact name.
 */
class SupplierOverduePurchaseOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Supplier nordic = store.createSupplier(new Supplier(0, "Nordic Supply", "orders@nordic.example", "Sweden", 3));
    private final Supplier baltic = store.createSupplier(new Supplier(0, "Baltic Trade", "sales@baltic.example", "Latvia", 5));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product bolt = store.createProduct(new Product(0, "B-1", "Bolt", "Fasteners", new BigDecimal("0.50"), "piece", true));

    private void order(String number, Supplier supplier, LocalDate expected, PurchaseOrderStatus status) {
        store.createPurchaseOrder(new PurchaseOrder(0, number, supplier.id(), LocalDate.of(2024, 7, 1), expected, status,
                List.of(new PurchaseOrderLine(widget.id(), 10, new BigDecimal("2.10")),
                        new PurchaseOrderLine(bolt.id(), 100, new BigDecimal("0.20")))));
    }

    private void createOrders() {
        order("PO-1", nordic, LocalDate.of(2024, 7, 20), PurchaseOrderStatus.SENT);
        order("PO-2", baltic, LocalDate.of(2024, 7, 10), PurchaseOrderStatus.SENT);
        order("PO-3", nordic, LocalDate.of(2024, 7, 5), PurchaseOrderStatus.DRAFT);
        order("PO-4", nordic, LocalDate.of(2024, 7, 6), PurchaseOrderStatus.RECEIVED);
        order("PO-5", nordic, LocalDate.of(2024, 7, 7), PurchaseOrderStatus.CANCELLED);
        order("PO-6", nordic, LocalDate.of(2024, 7, 12), PurchaseOrderStatus.SENT);
        order("PO-7", nordic, LocalDate.of(2024, 7, 8), PurchaseOrderStatus.SENT);
        order("PO-8", nordic, LocalDate.of(2024, 7, 8), PurchaseOrderStatus.SENT);
    }

    private static Method query() {
        List<Method> found = new ArrayList<>();
        for (Method method : DataStore.class.getMethods()) {
            String name = method.getName().toLowerCase();
            Class<?>[] parameters = method.getParameterTypes();
            if (Modifier.isPublic(method.getModifiers()) && name.contains("overdue") && name.contains("purchase")
                    && name.contains("supplier")
                    && (Arrays.equals(parameters, new Class<?>[]{long.class, LocalDate.class})
                    || Arrays.equals(parameters, new Class<?>[]{LocalDate.class, long.class}))
                    && List.class.isAssignableFrom(method.getReturnType())) {
                found.add(method);
            }
        }
        assertEquals(1, found.size(), "expected one query for the overdue purchase orders of a supplier, found " + found);
        return found.get(0);
    }

    private List<PurchaseOrder> overdue(long supplierId, LocalDate date) throws Exception {
        Method query = query();
        Object result = query.getParameterTypes()[0] == long.class ? query.invoke(store, supplierId, date)
                : query.invoke(store, date, supplierId);
        List<PurchaseOrder> orders = new ArrayList<>();
        for (Object order : (List<?>) result) {
            orders.add((PurchaseOrder) order);
        }
        return orders;
    }

    private static List<String> numbers(List<PurchaseOrder> orders) {
        return orders.stream().map(PurchaseOrder::number).toList();
    }

    @Test
    void listsTheSentOrdersOfTheSupplierExpectedBeforeTheDate() throws Exception {
        createOrders();
        List<PurchaseOrder> orders = overdue(nordic.id(), LocalDate.of(2024, 7, 13));
        assertEquals(List.of("PO-7", "PO-8", "PO-6"), numbers(orders));
        assertEquals(2, orders.get(0).lines().size());
        assertEquals(100, orders.get(0).lines().get(1).quantity());
        assertEquals(List.of("PO-2"), numbers(overdue(baltic.id(), LocalDate.of(2024, 7, 13))));
    }

    @Test
    void anOrderExpectedOnTheDateIsNotYetOverdue() throws Exception {
        createOrders();
        assertEquals(List.of("PO-7", "PO-8"), numbers(overdue(nordic.id(), LocalDate.of(2024, 7, 12))));
        assertEquals(List.of(), numbers(overdue(nordic.id(), LocalDate.of(2024, 7, 8))));
        assertEquals(List.of(), numbers(overdue(baltic.id(), LocalDate.of(2024, 7, 10))));
        assertEquals(List.of(), numbers(overdue(999, LocalDate.of(2025, 1, 1))));
    }
}
