package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.WeekdayCustomers;
import com.acme.backoffice.service.WeekdayCustomersService;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeekdayCustomersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final WeekdayCustomersService service = new WeekdayCustomersService(store);
    private int counter;

    private void customer(Region region, LocalDate created) {
        counter++;
        Customer customer = store.createCustomer(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, "UK", region, CustomerStatus.ACTIVE));
        if (created != null) {
            store.updateCustomerCreatedDate(customer.id(), created);
        }
    }

    @Test
    void countsCustomersPerWeekdayOverallAndPerRegion() {
        // 2024-01-01 is a Monday
        customer(Region.NORTH, LocalDate.of(2024, 1, 1));
        customer(Region.NORTH, LocalDate.of(2024, 1, 8));
        customer(Region.SOUTH, LocalDate.of(2024, 1, 15));
        customer(Region.EAST, LocalDate.of(2024, 1, 7));
        customer(Region.WEST, LocalDate.of(2024, 1, 3));
        customer(Region.NORTH, LocalDate.of(2023, 12, 31));
        customer(Region.NORTH, LocalDate.of(2024, 1, 16));
        customer(Region.NORTH, null);

        List<WeekdayCustomers> days = service.report(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15));

        assertEquals(7, days.size());
        assertEquals(DayOfWeek.MONDAY, days.get(0).weekday());
        assertEquals(3, days.get(0).total());
        assertEquals(2, days.get(0).perRegion().get(Region.NORTH));
        assertEquals(1, days.get(0).perRegion().get(Region.SOUTH));
        assertEquals(0, days.get(0).perRegion().get(Region.EAST));
        assertEquals(0, days.get(1).total());
        assertEquals(DayOfWeek.WEDNESDAY, days.get(2).weekday());
        assertEquals(1, days.get(2).perRegion().get(Region.WEST));
        assertEquals(DayOfWeek.SUNDAY, days.get(6).weekday());
        assertEquals(1, days.get(6).total());
        assertEquals(1, days.get(6).perRegion().get(Region.EAST));
    }

    @Test
    void emptyRangeHasSevenEmptyDays() {
        List<WeekdayCustomers> days = service.report(LocalDate.of(2030, 1, 1), LocalDate.of(2030, 1, 31));

        assertEquals(7, days.size());
        for (WeekdayCustomers day : days) {
            assertEquals(0, day.total());
            assertEquals(4, day.perRegion().size());
        }
    }

    @Test
    void rejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> service.report(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.report(null, LocalDate.of(2024, 1, 1)));
    }
}
