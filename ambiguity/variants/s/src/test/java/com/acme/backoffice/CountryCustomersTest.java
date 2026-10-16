package com.acme.backoffice;

import com.acme.backoffice.model.CountryCustomerCount;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.service.CountryCustomersService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountryCustomersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CountryCustomersService service = new CountryCustomersService(store);
    private int counter;

    private void customer(String country, LocalDate created) {
        counter++;
        Customer customer = store.createCustomer(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, country, Region.NORTH, CustomerStatus.ACTIVE));
        if (created != null) {
            store.updateCustomerCreatedDate(customer.id(), created);
        }
    }

    private void fill() {
        customer("UK", LocalDate.of(2024, 1, 20));
        customer("UK", LocalDate.of(2024, 1, 1));
        customer("UK", LocalDate.of(2024, 1, 31));
        customer("UK", LocalDate.of(2023, 12, 31));
        customer("UK", LocalDate.of(2024, 2, 1));
        customer("UK", null);
        customer("DE", LocalDate.of(2024, 1, 10));
        customer("FR", LocalDate.of(2024, 1, 11));
        customer("FR", LocalDate.of(2024, 1, 12));
        customer("AT", LocalDate.of(2024, 3, 1));
    }

    @Test
    void listsCustomersOfCountryInPeriodOldestFirst() {
        fill();

        assertEquals(List.of("C2", "C1", "C3"),
                service.customers("UK", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)).stream().map(Customer::name).toList());
        assertEquals(List.of(), service.customers("AT", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void countsCustomersPerCountryHighestFirst() {
        fill();

        assertEquals(List.of(new CountryCustomerCount("UK", 3), new CountryCustomerCount("FR", 2), new CountryCustomerCount("DE", 1)),
                service.countPerCountry(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void emptyPeriodGivesEmptyResults() {
        assertEquals(List.of(), service.countPerCountry(LocalDate.of(2030, 1, 1), LocalDate.of(2030, 1, 31)));
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.countPerCountry(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.customers("UK", null, LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.customers(" ", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 2)));
    }
}
