package com.acme.backoffice.model;

import java.time.LocalDate;

/** The decision of a sales manager on the manual discounts of a sales order: approved or rejected. */
public record OrderApproval(long orderId, boolean approved, String approver, LocalDate date) {
}
