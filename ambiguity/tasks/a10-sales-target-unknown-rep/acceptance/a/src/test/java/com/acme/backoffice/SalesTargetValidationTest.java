package com.acme.backoffice;

import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.service.SalesTargetService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SalesTargetValidationTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SalesTargetService service = new SalesTargetService(store);
    private final SalesRep anna = store.createSalesRep(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
    private final YearMonth june = YearMonth.of(2024, 6);

    @Test
    void targetForAnUnknownSalesRepIsRefusedAsInvalidInput() {
        long unknown = anna.id() + 1000;
        IllegalArgumentException refused = assertThrows(IllegalArgumentException.class,
                () -> service.setTarget(unknown, june, new BigDecimal("100.00")));
        assertTrue(refused.getMessage().contains(String.valueOf(unknown)), "the message should name the rep: " + refused.getMessage());
        assertTrue(service.progress(june).isEmpty());
    }

    @Test
    void targetWithoutAMonthIsRefusedAsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.setTarget(anna.id(), null, new BigDecimal("100.00")));
        assertTrue(service.progress(june).isEmpty());
    }

    @Test
    void validTargetIsStillSaved() {
        service.setTarget(anna.id(), june, new BigDecimal("100.00"));
        assertEquals(1, service.progress(june).size());
        assertEquals(0, new BigDecimal("100.00").compareTo(service.progress(june).get(0).target()));
    }
}
