package com.acme.backoffice.model;

import java.time.LocalDate;

/** A confirmed order that could not be shipped because of short stock and is waiting for stock. */
public record BackOrder(long id, long orderId, String orderNumber, long customerId, LocalDate backOrderDate) {
}
