package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.Region;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Finds the active customers that have not ordered for a number of days. */
public class DormantCustomerService {
    private final DataStore store;

    public DormantCustomerService(DataStore store) {
        this.store = store;
    }

    /** The active customers without a non-cancelled order in the days days before the date (the date itself not included). */
    public List<Customer> listDormant(LocalDate asOf, int days) {
        return store.customers().listActiveWithoutOrders(asOf.minusDays(days), asOf.minusDays(1));
    }

    /** The dormant customers per region; every region is present, with an empty list if it has none. */
    public Map<Region, List<Customer>> listDormantPerRegion(LocalDate asOf, int days) {
        Map<Region, List<Customer>> perRegion = new EnumMap<>(Region.class);
        for (Region region : Region.values()) {
            perRegion.put(region, new ArrayList<>());
        }
        for (Customer customer : listDormant(asOf, days)) {
            perRegion.get(customer.region()).add(customer);
        }
        return perRegion;
    }
}
