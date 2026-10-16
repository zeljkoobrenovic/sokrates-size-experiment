package com.acme.backoffice;

import com.acme.backoffice.model.TaxRate;

import java.time.LocalDate;
import java.util.Optional;

/** VAT (tax) rates per country, each valid from a date on until the next one. */
public final class TaxRateStore {
    private final Jdbc jdbc;

    TaxRateStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    /** Sets the VAT rate of the country valid from the date on, replacing a rate set for the same date. */
    public TaxRate save(TaxRate rate) {
        // Two statements without a transaction, as in the original.
        jdbc.update("Cannot save tax rate",
                "MERGE INTO tax_rate (country, valid_from, rate_percent) KEY (country, valid_from) VALUES (?, ?, ?)",
                rate.country(), rate.validFrom(), rate.ratePercent());
        long id = jdbc.single("Cannot save tax rate", "SELECT id FROM tax_rate WHERE country = ? AND valid_from = ?",
                rs -> rs.getLong("id"), rate.country(), rate.validFrom());
        return new TaxRate(id, rate.country(), rate.validFrom(), rate.ratePercent());
    }

    /** The VAT rate of the country that applies on the date: the latest one valid on or before it, if any. */
    public Optional<TaxRate> findValidOn(String country, LocalDate asOf) {
        return jdbc.first("Cannot find tax rate", "SELECT id, country, valid_from, rate_percent FROM tax_rate "
                        + "WHERE country = ? AND valid_from <= ? ORDER BY valid_from DESC LIMIT 1",
                rs -> new TaxRate(rs.getLong("id"), rs.getString("country"), rs.getObject("valid_from", LocalDate.class),
                        rs.getBigDecimal("rate_percent")),
                country, asOf);
    }
}
