package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Payment(long id, long invoiceId, LocalDate paymentDate, BigDecimal amount) {
}
