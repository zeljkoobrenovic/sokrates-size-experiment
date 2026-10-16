package com.acme.backoffice.model;

import java.time.LocalDate;
import java.util.List;

public record SalesOrder(long id, String number, long customerId, LocalDate orderDate, OrderStatus status,
                         List<OrderLine> lines) {
}
