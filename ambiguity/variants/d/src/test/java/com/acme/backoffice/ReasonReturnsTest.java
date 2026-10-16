package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ReasonReturns;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionReasonReturns;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.Warehouse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReasonReturnsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Warehouse warehouse = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Product bolt = store.createProduct(new Product(0, "B-1", "Bolt", "Fasteners", new BigDecimal("2.00"), "piece", true));
    private final SalesOrder northOrder;
    private final SalesOrder southOrder;
    private int counter;

    ReasonReturnsTest() {
        Customer north = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer south = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        northOrder = store.createSalesOrder(new SalesOrder(0, "SO-1", north.id(), LocalDate.of(2024, 1, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(bolt.id(), 50, new BigDecimal("2.00")))));
        southOrder = store.createSalesOrder(new SalesOrder(0, "SO-2", south.id(), LocalDate.of(2024, 1, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(bolt.id(), 50, new BigDecimal("3.00")))));
    }

    private void giveBack(SalesOrder order, LocalDate date, String reason, int quantity) {
        counter++;
        store.createSalesReturn(new SalesReturn(0, "R-" + counter, order.id(), date, reason,
                List.of(new ReturnLine(bolt.id(), quantity))), warehouse.id());
    }

    @Test
    void countsAndValuesReturnsPerReasonBetweenTwoDatesInclusive() {
        giveBack(northOrder, LocalDate.of(2024, 3, 1), "Damaged", 3);
        giveBack(southOrder, LocalDate.of(2024, 3, 31), "Damaged", 2);
        giveBack(northOrder, LocalDate.of(2024, 3, 10), "Wrong item", 10);
        giveBack(northOrder, LocalDate.of(2024, 2, 29), "Damaged", 9);
        giveBack(northOrder, LocalDate.of(2024, 4, 1), "Late", 9);
        List<ReasonReturns> result = store.findSalesReturnCountAndValuePerReason(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of("Wrong item", "Damaged"), result.stream().map(ReasonReturns::reason).toList());
        assertEquals(List.of(1, 2), result.stream().map(ReasonReturns::returnCount).toList());
        assertEquals(new BigDecimal("20.00"), result.get(0).returnedValue());
        assertEquals(new BigDecimal("12.00"), result.get(1).returnedValue());
        assertTrue(store.findSalesReturnCountAndValuePerReason(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31)).isEmpty());
    }

    @Test
    void splitsReturnsPerReasonByRegionOfTheCustomer() {
        giveBack(northOrder, LocalDate.of(2024, 3, 1), "Damaged", 3);
        giveBack(southOrder, LocalDate.of(2024, 3, 5), "Damaged", 2);
        giveBack(southOrder, LocalDate.of(2024, 3, 6), "Damaged", 1);
        giveBack(southOrder, LocalDate.of(2024, 3, 7), "Late", 5);
        List<RegionReasonReturns> result = store.findSalesReturnCountAndValuePerRegionAndReason(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of(Region.NORTH, Region.SOUTH, Region.SOUTH), result.stream().map(RegionReasonReturns::region).toList());
        assertEquals(List.of("Damaged", "Late", "Damaged"), result.stream().map(RegionReasonReturns::reason).toList());
        assertEquals(List.of(1, 1, 2), result.stream().map(RegionReasonReturns::returnCount).toList());
        assertEquals(List.of(new BigDecimal("6.00"), new BigDecimal("15.00"), new BigDecimal("9.00")),
                result.stream().map(RegionReasonReturns::returnedValue).toList());
    }
}
