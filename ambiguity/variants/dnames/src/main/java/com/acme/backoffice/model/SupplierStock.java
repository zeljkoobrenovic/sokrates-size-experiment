package com.acme.backoffice.model;

import java.util.List;

/** The stock of the products a supplier delivers, per warehouse. */
public record SupplierStock(Supplier supplier, List<SupplierWarehouseStock> warehouses) {
}
