package com.acme.backoffice;

import com.acme.backoffice.model.AddressType;
import com.acme.backoffice.model.CustomerAddress;
import com.acme.backoffice.model.CustomerContact;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionContact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * How to reach a customer: its addresses (billing and shipping, including the default shipping address) and its
 * contact persons (by customer, by e-mail, by role and per region).
 */
public final class CustomerContactStore {
    private static final String ADDRESS_COLUMNS = "id, customer_id, type, street, city, postal_code, country, default_shipping";
    private static final String CONTACT_COLUMNS = "id, customer_id, name, role, email, phone";

    private final Jdbc jdbc;

    CustomerContactStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    // ---- addresses ----

    public CustomerAddress createAddress(CustomerAddress address) {
        long id = jdbc.insert("Cannot create customer address",
                "INSERT INTO customer_address (customer_id, type, street, city, postal_code, country, default_shipping) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                address.customerId(), address.type().name(), address.street(), address.city(), address.postalCode(),
                address.country(), address.defaultShipping());
        return new CustomerAddress(id, address.customerId(), address.type(), address.street(), address.city(),
                address.postalCode(), address.country(), address.defaultShipping());
    }

    /** Updates type, street, city, postal code and country; the default shipping flag is left as it is. */
    public void updateAddress(CustomerAddress address) {
        jdbc.update("Cannot update customer address",
                "UPDATE customer_address SET type = ?, street = ?, city = ?, postal_code = ?, country = ? WHERE id = ?",
                address.type().name(), address.street(), address.city(), address.postalCode(), address.country(), address.id());
    }

    public Optional<CustomerAddress> findAddressById(long id) {
        return jdbc.first("Cannot find customer address", "SELECT " + ADDRESS_COLUMNS + " FROM customer_address WHERE id = ?",
                CustomerContactStore::address, id);
    }

    /** The addresses of the customer, in id order. */
    public List<CustomerAddress> listAddresses(long customerId) {
        return jdbc.list("Cannot list customer addresses",
                "SELECT " + ADDRESS_COLUMNS + " FROM customer_address WHERE customer_id = ? ORDER BY id",
                CustomerContactStore::address, customerId);
    }

    /** Makes the given address the only default among the shipping addresses of the customer. */
    public void setDefaultShippingAddress(long customerId, long addressId) {
        jdbc.update("Cannot set default shipping address",
                "UPDATE customer_address SET default_shipping = (id = ?) WHERE customer_id = ? AND type = 'SHIPPING'",
                addressId, customerId);
    }

    // ---- contact persons ----

    public CustomerContact createContact(CustomerContact contact) {
        long id = jdbc.insert("Cannot create customer contact",
                "INSERT INTO customer_contact (customer_id, name, role, email, phone) VALUES (?, ?, ?, ?, ?)",
                contact.customerId(), contact.name(), contact.role(), contact.email(), contact.phone());
        return new CustomerContact(id, contact.customerId(), contact.name(), contact.role(), contact.email(), contact.phone());
    }

    /** The contact with the e-mail address, ignoring case (the first one found if several match). */
    public Optional<CustomerContact> findContactByEmail(String email) {
        return jdbc.first("Cannot find customer contact",
                "SELECT " + CONTACT_COLUMNS + " FROM customer_contact WHERE LOWER(email) = LOWER(?)",
                CustomerContactStore::contact, email);
    }

    /** The contacts of the customer, in id order. */
    public List<CustomerContact> listContacts(long customerId) {
        return jdbc.list("Cannot list customer contacts",
                "SELECT " + CONTACT_COLUMNS + " FROM customer_contact WHERE customer_id = ? ORDER BY id",
                CustomerContactStore::contact, customerId);
    }

    /** The contacts with the role, ignoring case, of all customers, sorted by customer id, then contact id. */
    public List<CustomerContact> listContactsWithRole(String role) {
        return jdbc.list("Cannot find customer contacts by role",
                "SELECT " + CONTACT_COLUMNS + " FROM customer_contact WHERE LOWER(role) = LOWER(?) ORDER BY customer_id, id",
                CustomerContactStore::contact, role);
    }

    /** Contacts of the customers of the region, sorted by role ignoring case, then customer name, customer id and contact id. */
    public List<RegionContact> listContactsInRegion(Region region) {
        return jdbc.list("Cannot list region contacts",
                "SELECT k.id, k.customer_id, k.name, k.role, k.email, k.phone, c.name AS customer_name "
                        + "FROM customer_contact k JOIN customer c ON c.id = k.customer_id WHERE c.region = ? "
                        + "ORDER BY LOWER(k.role), c.name, c.id, k.id",
                rs -> new RegionContact(rs.getString("customer_name"), contact(rs)), region.name());
    }

    private static CustomerAddress address(ResultSet rs) throws SQLException {
        return new CustomerAddress(rs.getLong("id"), rs.getLong("customer_id"), AddressType.valueOf(rs.getString("type")),
                rs.getString("street"), rs.getString("city"), rs.getString("postal_code"), rs.getString("country"),
                rs.getBoolean("default_shipping"));
    }

    private static CustomerContact contact(ResultSet rs) throws SQLException {
        return new CustomerContact(rs.getLong("id"), rs.getLong("customer_id"), rs.getString("name"), rs.getString("role"),
                rs.getString("email"), rs.getString("phone"));
    }
}
