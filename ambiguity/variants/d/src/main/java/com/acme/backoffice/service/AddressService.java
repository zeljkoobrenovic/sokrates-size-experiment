package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.AddressType;
import com.acme.backoffice.model.CustomerAddress;

import java.util.List;

/** Customer addresses: a customer has any number of addresses and at most one default shipping address. */
public class AddressService {
    private final DataStore store;

    public AddressService(DataStore store) {
        this.store = store;
    }

    /** Adds an address to an existing customer. A new address is never the default. */
    public CustomerAddress addAddress(long customerId, AddressType type, String street, String city, String postalCode, String country) {
        validate(type, street, city, postalCode, country);
        if (store.findCustomerById(customerId).isEmpty()) {
            throw new IllegalArgumentException("Unknown customer: " + customerId);
        }
        return store.createCustomerAddress(new CustomerAddress(0, customerId, type, street, city, postalCode, country, false));
    }

    /** Changes type and text of an address. The default address must stay a shipping address. */
    public void updateAddress(long addressId, AddressType type, String street, String city, String postalCode, String country) {
        validate(type, street, city, postalCode, country);
        CustomerAddress address = find(addressId);
        if (address.defaultShipping() && type != AddressType.SHIPPING) {
            throw new IllegalStateException("The default shipping address must be a shipping address");
        }
        store.updateCustomerAddress(new CustomerAddress(addressId, address.customerId(), type, street, city, postalCode, country,
                address.defaultShipping()));
    }

    /** Makes the shipping address the only default of its customer. */
    public void setDefaultShippingAddress(long addressId) {
        CustomerAddress address = find(addressId);
        if (address.type() != AddressType.SHIPPING) {
            throw new IllegalStateException("Address " + addressId + " is not a shipping address");
        }
        store.setDefaultShippingAddress(address.customerId(), addressId);
    }

    public List<CustomerAddress> listAddresses(long customerId) {
        return store.findCustomerAddresses(customerId);
    }

    private CustomerAddress find(long addressId) {
        return store.findCustomerAddressById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown address: " + addressId));
    }

    private void validate(AddressType type, String street, String city, String postalCode, String country) {
        if (type == null) {
            throw new IllegalArgumentException("Address type is required");
        }
        if (street == null || street.isBlank() || city == null || city.isBlank() || postalCode == null || postalCode.isBlank()
                || country == null || country.isBlank()) {
            throw new IllegalArgumentException("Street, city, postal code and country are required");
        }
    }
}
