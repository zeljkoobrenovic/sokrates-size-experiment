package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.service.SupportTicketService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The support tickets of the customers of a country opened between two dates, and the number of such tickets per
 * country. The two new queries are found by what they are (public data-access queries about tickets and countries,
 * one taking a country and two dates, one taking two dates), not by their exact names; a count row is read by its
 * parts (the country and the number).
 */
class CountryTicketsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SupportTicketService tickets = new SupportTicketService(store);
    private int counter;

    private Customer customer(String country) {
        counter++;
        return store.createCustomer(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, country, Region.NORTH, CustomerStatus.ACTIVE));
    }

    private void ticket(String number, Customer customer, LocalDate opened) {
        tickets.create(number, customer.id(), null, "Subject " + number, TicketPriority.NORMAL, opened);
    }

    private static Method query(Class<?>... parameters) {
        List<Method> found = new ArrayList<>();
        for (Method method : DataStore.class.getMethods()) {
            String name = method.getName().toLowerCase();
            if (Modifier.isPublic(method.getModifiers()) && name.contains("ticket") && name.contains("country")
                    && Arrays.equals(method.getParameterTypes(), parameters)
                    && List.class.isAssignableFrom(method.getReturnType())) {
                found.add(method);
            }
        }
        assertEquals(1, found.size(), "expected one query about tickets and countries taking " + Arrays.toString(parameters)
                + ", found " + found);
        return found.get(0);
    }

    private List<String> ticketsOf(String country, LocalDate from, LocalDate to) throws Exception {
        List<?> rows = (List<?>) query(String.class, LocalDate.class, LocalDate.class).invoke(store, country, from, to);
        return rows.stream().map(row -> ((SupportTicket) row).number()).toList();
    }

    /** Each count row as "country=count". */
    private List<String> countPerCountry(LocalDate from, LocalDate to) throws Exception {
        List<?> rows = (List<?>) query(LocalDate.class, LocalDate.class).invoke(store, from, to);
        List<String> result = new ArrayList<>();
        for (Object row : rows) {
            assertTrue(row.getClass().isRecord(), "a row should be a record");
            String country = null;
            Long count = null;
            for (RecordComponent component : row.getClass().getRecordComponents()) {
                Object value = component.getAccessor().invoke(row);
                if (value instanceof String text) {
                    country = text;
                } else if (value instanceof Number number) {
                    count = number.longValue();
                }
            }
            result.add(country + "=" + count);
        }
        return result;
    }

    private void fill() {
        Customer uk1 = customer("UK");
        Customer uk2 = customer("UK");
        Customer usa = customer("USA");
        Customer norway = customer("Norway");
        customer("Spain");
        ticket("T-1", uk1, LocalDate.of(2024, 3, 10));
        ticket("T-2", uk2, LocalDate.of(2024, 3, 1));
        ticket("T-3", uk1, LocalDate.of(2024, 3, 31));
        ticket("T-4", usa, LocalDate.of(2024, 3, 5));
        ticket("T-5", usa, LocalDate.of(2024, 3, 5));
        ticket("T-6", norway, LocalDate.of(2024, 3, 20));
        ticket("T-7", uk1, LocalDate.of(2024, 2, 29));
        ticket("T-8", usa, LocalDate.of(2024, 4, 1));
        ticket("T-9", uk2, LocalDate.of(2024, 3, 10));
        tickets.changeStatus("T-1", TicketStatus.CLOSED, LocalDate.of(2024, 3, 12));
        tickets.changeStatus("T-4", TicketStatus.IN_PROGRESS, LocalDate.of(2024, 3, 6));
    }

    @Test
    void listsTicketsOfTheCustomersOfACountryOldestFirst() throws Exception {
        fill();
        LocalDate from = LocalDate.of(2024, 3, 1);
        LocalDate to = LocalDate.of(2024, 3, 31);
        assertEquals(List.of("T-2", "T-1", "T-9", "T-3"), ticketsOf("UK", from, to));
        assertEquals(List.of("T-4", "T-5"), ticketsOf("USA", from, to));
        assertEquals(List.of(), ticketsOf("Spain", from, to));
    }

    @Test
    void countsTicketsPerCountryHighestFirst() throws Exception {
        fill();
        assertEquals(List.of("UK=4", "USA=2", "Norway=1"), countPerCountry(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)));
        assertEquals(List.of("USA=1"), countPerCountry(LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 30)));
        assertEquals(List.of(), countPerCountry(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31)));
    }

    @Test
    void equalCountsAreSortedByCountry() throws Exception {
        Customer usa = customer("USA");
        Customer belgium = customer("Belgium");
        ticket("T-1", usa, LocalDate.of(2024, 5, 1));
        ticket("T-2", belgium, LocalDate.of(2024, 5, 2));
        assertEquals(List.of("Belgium=1", "USA=1"), countPerCountry(LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 31)));
    }
}
