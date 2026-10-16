-- BackOffice database schema (H2). Tables are added here as features need them.

CREATE TABLE sales_rep (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL,
    region VARCHAR(10) NOT NULL
);

CREATE TABLE customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL,
    phone VARCHAR(50),
    country VARCHAR(100) NOT NULL,
    region VARCHAR(10) NOT NULL,
    status VARCHAR(10) NOT NULL,
    sales_rep_id BIGINT REFERENCES sales_rep (id),
    credit_limit DECIMAL(14, 2) CHECK (credit_limit >= 0),
    created_date DATE
);

CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    category VARCHAR(100) NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    unit_of_measure VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE supplier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    contact_email VARCHAR(200) NOT NULL,
    country VARCHAR(100) NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5)
);

CREATE TABLE warehouse (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    region VARCHAR(10) NOT NULL
);

CREATE TABLE stock (
    warehouse_id BIGINT NOT NULL REFERENCES warehouse (id),
    product_id BIGINT NOT NULL REFERENCES product (id),
    quantity INT NOT NULL CHECK (quantity >= 0),
    PRIMARY KEY (warehouse_id, product_id)
);

CREATE TABLE sales_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL REFERENCES customer (id),
    order_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE sales_order_line (
    order_id BIGINT NOT NULL REFERENCES sales_order (id),
    line_no INT NOT NULL,
    product_id BIGINT NOT NULL REFERENCES product (id),
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(12, 2) NOT NULL,
    promotion_code VARCHAR(50),
    PRIMARY KEY (order_id, line_no)
);

CREATE TABLE sales_order_status_change (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES sales_order (id),
    status VARCHAR(20) NOT NULL,
    change_date DATE NOT NULL
);

CREATE TABLE invoice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(50) NOT NULL UNIQUE,
    order_id BIGINT NOT NULL REFERENCES sales_order (id),
    customer_id BIGINT NOT NULL REFERENCES customer (id),
    invoice_date DATE NOT NULL,
    due_date DATE NOT NULL,
    amount DECIMAL(14, 2) NOT NULL,
    status VARCHAR(10) NOT NULL
);

CREATE TABLE payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_id BIGINT NOT NULL REFERENCES invoice (id),
    payment_date DATE NOT NULL,
    amount DECIMAL(14, 2) NOT NULL CHECK (amount > 0)
);

CREATE TABLE carrier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    service_level VARCHAR(10) NOT NULL,
    price_per_kg DECIMAL(10, 2) NOT NULL CHECK (price_per_kg >= 0),
    promised_days INT NOT NULL DEFAULT 7 CHECK (promised_days >= 0)
);

CREATE TABLE shipment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE REFERENCES sales_order (id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouse (id),
    carrier_id BIGINT NOT NULL REFERENCES carrier (id),
    tracking_number VARCHAR(100) NOT NULL,
    ship_date DATE NOT NULL,
    delivery_date DATE
);

CREATE TABLE purchase_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(50) NOT NULL UNIQUE,
    supplier_id BIGINT NOT NULL REFERENCES supplier (id),
    order_date DATE NOT NULL,
    expected_date DATE NOT NULL,
    status VARCHAR(10) NOT NULL,
    received_date DATE,
    warehouse_id BIGINT REFERENCES warehouse (id),
    received_warehouse_id BIGINT REFERENCES warehouse (id)
);

CREATE TABLE purchase_order_line (
    order_id BIGINT NOT NULL REFERENCES purchase_order (id),
    line_no INT NOT NULL,
    product_id BIGINT NOT NULL REFERENCES product (id),
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_cost DECIMAL(12, 2) NOT NULL,
    PRIMARY KEY (order_id, line_no)
);

CREATE TABLE customer_address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customer (id),
    type VARCHAR(10) NOT NULL,
    street VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL,
    default_shipping BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE sales_return (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(50) NOT NULL UNIQUE,
    order_id BIGINT NOT NULL REFERENCES sales_order (id),
    return_date DATE NOT NULL,
    reason VARCHAR(500) NOT NULL,
    warehouse_id BIGINT REFERENCES warehouse (id)
);

CREATE TABLE sales_return_line (
    return_id BIGINT NOT NULL REFERENCES sales_return (id),
    line_no INT NOT NULL,
    product_id BIGINT NOT NULL REFERENCES product (id),
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (return_id, line_no)
);

CREATE TABLE credit_note (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL REFERENCES customer (id),
    return_id BIGINT NOT NULL UNIQUE REFERENCES sales_return (id),
    credit_date DATE NOT NULL,
    amount DECIMAL(12, 2) NOT NULL
);

CREATE TABLE promotion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    discount_percent DECIMAL(5, 2) NOT NULL CHECK (discount_percent > 0 AND discount_percent <= 100),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    category VARCHAR(100),
    CHECK (end_date >= start_date)
);

CREATE TABLE promotion_region (
    promotion_id BIGINT PRIMARY KEY REFERENCES promotion (id),
    region VARCHAR(10) NOT NULL
);

CREATE TABLE customer_price (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customer (id),
    product_id BIGINT NOT NULL REFERENCES product (id),
    unit_price DECIMAL(12, 2) NOT NULL CHECK (unit_price >= 0),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    CHECK (end_date >= start_date)
);

CREATE TABLE category (
    code VARCHAR(100) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    parent_code VARCHAR(100) REFERENCES category (code)
);

CREATE TABLE reorder_level (
    warehouse_id BIGINT NOT NULL REFERENCES warehouse (id),
    product_id BIGINT NOT NULL REFERENCES product (id),
    level INT NOT NULL CHECK (level >= 0),
    PRIMARY KEY (warehouse_id, product_id)
);

CREATE TABLE supplier_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id BIGINT NOT NULL REFERENCES supplier (id),
    product_id BIGINT NOT NULL REFERENCES product (id),
    lead_time_days INT NOT NULL CHECK (lead_time_days >= 0),
    last_unit_cost DECIMAL(12, 2) NOT NULL CHECK (last_unit_cost >= 0),
    UNIQUE (supplier_id, product_id)
);

CREATE TABLE order_note (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES sales_order (id),
    author VARCHAR(200) NOT NULL,
    note_date DATE NOT NULL,
    text VARCHAR(2000) NOT NULL
);

CREATE TABLE stock_movement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product (id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouse (id),
    movement_date DATE NOT NULL,
    quantity_change INT NOT NULL,
    reason VARCHAR(10) NOT NULL
);

CREATE TABLE stock_adjustment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product (id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouse (id),
    adjustment_date DATE NOT NULL,
    quantity_before INT NOT NULL CHECK (quantity_before >= 0),
    counted_quantity INT NOT NULL CHECK (counted_quantity >= 0),
    reason VARCHAR(500) NOT NULL
);

CREATE TABLE back_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE REFERENCES sales_order (id),
    back_order_date DATE NOT NULL
);

CREATE TABLE shipment_part (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    line_no INT NOT NULL,
    warehouse_id BIGINT NOT NULL REFERENCES warehouse (id),
    carrier_id BIGINT NOT NULL REFERENCES carrier (id),
    tracking_number VARCHAR(100) NOT NULL,
    ship_date DATE NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    FOREIGN KEY (order_id, line_no) REFERENCES sales_order_line (order_id, line_no)
);

CREATE TABLE product_price_change (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product (id),
    change_date DATE NOT NULL,
    old_price DECIMAL(12, 2) NOT NULL,
    new_price DECIMAL(12, 2) NOT NULL
);

CREATE TABLE exchange_rate (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    currency VARCHAR(3) NOT NULL CHECK (currency <> 'EUR'),
    rate_date DATE NOT NULL,
    per_eur DECIMAL(14, 6) NOT NULL CHECK (per_eur > 0),
    UNIQUE (currency, rate_date)
);

CREATE TABLE customer_currency (
    customer_id BIGINT PRIMARY KEY REFERENCES customer (id),
    currency VARCHAR(3) NOT NULL
);

CREATE TABLE invoice_currency (
    invoice_id BIGINT PRIMARY KEY REFERENCES invoice (id),
    currency VARCHAR(3) NOT NULL,
    amount DECIMAL(14, 2) NOT NULL
);

CREATE TABLE tax_rate (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    country VARCHAR(100) NOT NULL,
    valid_from DATE NOT NULL,
    rate_percent DECIMAL(5, 2) NOT NULL CHECK (rate_percent >= 0),
    UNIQUE (country, valid_from)
);

CREATE TABLE warehouse_transfer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(50) NOT NULL UNIQUE,
    from_warehouse_id BIGINT NOT NULL REFERENCES warehouse (id),
    to_warehouse_id BIGINT NOT NULL REFERENCES warehouse (id),
    transfer_date DATE NOT NULL,
    CHECK (from_warehouse_id <> to_warehouse_id)
);

CREATE TABLE warehouse_transfer_line (
    transfer_id BIGINT NOT NULL REFERENCES warehouse_transfer (id),
    line_no INT NOT NULL,
    product_id BIGINT NOT NULL REFERENCES product (id),
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (transfer_id, line_no)
);

CREATE TABLE order_line_discount (
    order_id BIGINT NOT NULL,
    line_no INT NOT NULL,
    discount_percent DECIMAL(5, 2) NOT NULL CHECK (discount_percent >= 0 AND discount_percent <= 100),
    PRIMARY KEY (order_id, line_no),
    FOREIGN KEY (order_id, line_no) REFERENCES sales_order_line (order_id, line_no)
);

CREATE TABLE order_approval (
    order_id BIGINT PRIMARY KEY REFERENCES sales_order (id),
    approved BOOLEAN NOT NULL,
    approver VARCHAR(200) NOT NULL,
    decision_date DATE NOT NULL
);

CREATE TABLE customer_contact (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customer (id),
    name VARCHAR(200) NOT NULL,
    role VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    phone VARCHAR(50)
);

CREATE TABLE support_ticket (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL REFERENCES customer (id),
    order_id BIGINT REFERENCES sales_order (id),
    subject VARCHAR(500) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    opened_date DATE NOT NULL,
    closed_date DATE,
    product_id BIGINT REFERENCES product (id)
);

CREATE TABLE bundle_component (
    bundle_id BIGINT NOT NULL REFERENCES product (id),
    component_id BIGINT NOT NULL REFERENCES product (id),
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (bundle_id, component_id),
    CHECK (bundle_id <> component_id)
);

CREATE TABLE order_cancellation (
    order_id BIGINT PRIMARY KEY REFERENCES sales_order (id),
    reason VARCHAR(20) NOT NULL,
    cancel_date DATE NOT NULL
);

CREATE TABLE sales_target (
    sales_rep_id BIGINT NOT NULL REFERENCES sales_rep (id),
    target_month DATE NOT NULL,
    target_amount DECIMAL(14, 2) NOT NULL CHECK (target_amount > 0),
    PRIMARY KEY (sales_rep_id, target_month)
);

CREATE TABLE audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(200) NOT NULL,
    change_date DATE NOT NULL,
    entity_type VARCHAR(20) NOT NULL,
    entity_id BIGINT NOT NULL,
    description VARCHAR(500) NOT NULL
);

CREATE TABLE customer_closure (
    customer_id BIGINT PRIMARY KEY REFERENCES customer (id),
    closed_date DATE NOT NULL
);

CREATE TABLE order_archive (
    order_id BIGINT PRIMARY KEY REFERENCES sales_order (id),
    archive_date DATE NOT NULL
);

CREATE TABLE region_manager (
    region VARCHAR(10) PRIMARY KEY,
    sales_rep_id BIGINT NOT NULL REFERENCES sales_rep (id)
);

CREATE TABLE order_channel (
    order_id BIGINT PRIMARY KEY REFERENCES sales_order (id),
    channel VARCHAR(20) NOT NULL
);

CREATE TABLE credit_note_application (
    credit_note_id BIGINT PRIMARY KEY REFERENCES credit_note (id),
    invoice_id BIGINT NOT NULL REFERENCES invoice (id),
    applied_date DATE NOT NULL
);

CREATE TABLE product_weight (
    product_id BIGINT PRIMARY KEY REFERENCES product (id),
    weight_kg DECIMAL(10, 3) NOT NULL CHECK (weight_kg >= 0)
);
