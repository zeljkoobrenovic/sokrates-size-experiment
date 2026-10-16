package com.acme.backoffice.model;

import java.time.LocalDate;

public record OrderStatusChange(long orderId, OrderStatus status, LocalDate changeDate) {
}
