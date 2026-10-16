package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.QuarterCustomers;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.service.QuarterCustomersService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuarterCustomersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final QuarterCustomersService service = new QuarterCustomersService(store);
    private int counter;

    private void customer(Region region, LocalDate created) {
        counter++;
        Customer customer = store.createCustomer(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, "UK", region, CustomerStatus.ACTIVE));
        if (created != null) {
            store.updateCustomerCreatedDate(customer.id(), created);
        }
    }

    @Test
    void countsCustomersPerQuarterOverallAndPerRegion() {
        customer(Region.NORTH, LocalDate.of(2024, 1, 1));
        customer(Region.NORTH, LocalDate.of(2024, 3, 31));
        customer(Region.SOUTH, LocalDate.of(2024, 3, 15));
        customer(Region.EAST, LocalDate.of(2024, 4, 1));
        customer(Region.WEST, LocalDate.of(2024, 12, 31));
        customer(Region.NORTH, LocalDate.of(2023, 12, 31));
        customer(Region.NORTH, LocalDate.of(2025, 1, 1));
        customer(Region.NORTH, null);

        List<QuarterCustomers> quarters = service.report(2024);

        assertEquals(4, quarters.size());
        assertEquals(1, quarters.get(0).quarter());
        assertEquals(3, quarters.get(0).total());
        assertEquals(2, quarters.get(0).perRegion().get(Region.NORTH));
        assertEquals(1, quarters.get(0).perRegion().get(Region.SOUTH));
        assertEquals(0, quarters.get(0).perRegion().get(Region.EAST));
        assertEquals(0, quarters.get(0).perRegion().get(Region.WEST));
        assertEquals(1, quarters.get(1).total());
        assertEquals(1, quarters.get(1).perRegion().get(Region.EAST));
        assertEquals(0, quarters.get(2).total());
        assertEquals(0, quarters.get(2).perRegion().get(Region.NORTH));
        assertEquals(4, quarters.get(3).quarter());
        assertEquals(1, quarters.get(3).total());
        assertEquals(1, quarters.get(3).perRegion().get(Region.WEST));
    }

    @Test
    void yearWithoutCustomersHasFourEmptyQuarters() {
        List<QuarterCustomers> quarters = service.report(2030);

        assertEquals(4, quarters.size());
        for (QuarterCustomers quarter : quarters) {
            assertEquals(0, quarter.total());
            assertEquals(4, quarter.perRegion().size());
        }
    }

    @Test
    void rejectsInvalidYear() {
        assertThrows(IllegalArgumentException.class, () -> service.report(0));
    }
}
