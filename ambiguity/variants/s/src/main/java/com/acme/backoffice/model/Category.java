package com.acme.backoffice.model;

/** A product category with a unique code. The parent code is null for a top-level category. Products refer to the code. */
public record Category(String code, String name, String parentCode) {
}
