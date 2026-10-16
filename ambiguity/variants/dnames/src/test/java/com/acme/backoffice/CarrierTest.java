package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.ServiceLevel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarrierTest {
    private final DataStore store = new DataStore(Database.openInMemory());

    @Test
    void createListAndFind() {
        Carrier road = store.createCarrier(new Carrier(0, "RoadRunner", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        Carrier fast = store.createCarrier(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50")));
        store.createCarrier(new Carrier(0, "AirDirect", ServiceLevel.EXPRESS, new BigDecimal("3.20")));
        assertTrue(road.id() > 0);
        assertEquals(List.of("AirDirect", "FastFreight", "RoadRunner"), store.listCarriersOrderedByName().stream().map(Carrier::name).toList());
        assertEquals(List.of("AirDirect", "FastFreight"), store.listCarriersWithServiceLevel(ServiceLevel.EXPRESS).stream().map(Carrier::name).toList());
        assertEquals(List.of(road), store.listCarriersWithServiceLevel(ServiceLevel.STANDARD));
        assertEquals(fast, store.findCarrierByName("FastFreight").orElseThrow());
        assertEquals(fast, store.findCarrierById(fast.id()).orElseThrow());
        assertTrue(store.findCarrierByName("Nobody").isEmpty());
        assertTrue(store.findCarrierById(999).isEmpty());
    }

    @Test
    void emptyList() {
        assertTrue(store.listCarriersOrderedByName().isEmpty());
        assertTrue(store.listCarriersWithServiceLevel(ServiceLevel.EXPRESS).isEmpty());
    }

    @Test
    void nameIsUnique() {
        store.createCarrier(new Carrier(0, "RoadRunner", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        assertThrows(IllegalStateException.class,
                () -> store.createCarrier(new Carrier(0, "RoadRunner", ServiceLevel.EXPRESS, new BigDecimal("1.00"))));
    }
}
