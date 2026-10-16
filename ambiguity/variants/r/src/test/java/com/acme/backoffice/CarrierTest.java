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
        Carrier road = store.carriers().create(new Carrier(0, "RoadRunner", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        Carrier fast = store.carriers().create(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50")));
        store.carriers().create(new Carrier(0, "AirDirect", ServiceLevel.EXPRESS, new BigDecimal("3.20")));
        assertTrue(road.id() > 0);
        assertEquals(List.of("AirDirect", "FastFreight", "RoadRunner"), store.carriers().listAllByName().stream().map(Carrier::name).toList());
        assertEquals(List.of("AirDirect", "FastFreight"), store.carriers().listByServiceLevel(ServiceLevel.EXPRESS).stream().map(Carrier::name).toList());
        assertEquals(List.of(road), store.carriers().listByServiceLevel(ServiceLevel.STANDARD));
        assertEquals(fast, store.carriers().findByName("FastFreight").orElseThrow());
        assertEquals(fast, store.carriers().findById(fast.id()).orElseThrow());
        assertTrue(store.carriers().findByName("Nobody").isEmpty());
        assertTrue(store.carriers().findById(999).isEmpty());
    }

    @Test
    void emptyList() {
        assertTrue(store.carriers().listAllByName().isEmpty());
        assertTrue(store.carriers().listByServiceLevel(ServiceLevel.EXPRESS).isEmpty());
    }

    @Test
    void nameIsUnique() {
        store.carriers().create(new Carrier(0, "RoadRunner", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        assertThrows(IllegalStateException.class,
                () -> store.carriers().create(new Carrier(0, "RoadRunner", ServiceLevel.EXPRESS, new BigDecimal("1.00"))));
    }
}
