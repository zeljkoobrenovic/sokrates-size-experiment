package com.acme.backoffice.model;

/** One line of a sales return: the product and the quantity that came back. */
public record ReturnLine(long productId, int quantity) {
}
