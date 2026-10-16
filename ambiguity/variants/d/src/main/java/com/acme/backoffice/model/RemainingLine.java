package com.acme.backoffice.model;

/** An order line that is not fully shipped yet; the line number counts from 1. */
public record RemainingLine(int lineNo, long productId, int ordered, int shipped, int remaining) {
}
