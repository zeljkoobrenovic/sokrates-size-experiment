# BackOffice feature requests, in the order they are implemented

## 01 Customers
We need to keep our customers: a customer has a name, an email, a phone number, a country, a region (North, South,
East, West) and a status (active, on hold, closed). Create, update, find by id and list all customers sorted by name.

## 02 Customer search
Let sales find customers: by part of the name (case-insensitive), by region, by country, and by status. Each search
returns the customers sorted by name.

## 03 Products
Keep the product catalogue: a product has a SKU (unique), a name, a category name, a unit price, a unit of measure and
an active flag. Create, update, find by SKU and list active products sorted by name.

## 04 Product search
Find products by part of the name, by category, and by price range (min and max inclusive). Only active products.

## 05 Suppliers
Suppliers have a name, a contact email, a country and a rating from 1 to 5. Create, update, list all, list by
country and list those with a rating of at least a given value.

## 06 Warehouses
We run several warehouses: each has a code, a name, a city and a region. Create, list all and find by code.

## 07 Stock levels
Track how many units of each product are in each warehouse. Set the quantity, add to it and take from it (never below
zero; taking more than there is fails), and read the quantity of a product in a warehouse.

## 08 Stock overview
Show the total stock of a product across all warehouses, the stock of all products in one warehouse, and the products
whose total stock is below a given threshold.

## 09 Sales orders
Customers place sales orders: an order has a number, a customer, an order date, a status (draft, confirmed, shipped,
delivered, cancelled) and order lines (product, quantity, unit price at the time of ordering). Create an order with its
lines, find an order with its lines, and list the orders of a customer, newest first.

## 10 Order status
Orders move through their statuses: confirm a draft, ship a confirmed order, deliver a shipped one, cancel a draft or
confirmed order. Other transitions are refused. Record the date of each change.

## 11 Open orders
Sales want to see the open orders (confirmed but not shipped): all of them, those of one customer, and those of
customers in a given region. Oldest first.

## 12 Order totals
Show the total value of an order (sum of quantity times unit price over its lines), and the total order value of a
customer over all non-cancelled orders.

## 13 Orders by period
List the orders placed between two dates (inclusive), all of them and only those with a given status, sorted by date.

## 14 Invoices
When an order is shipped we invoice it: an invoice has a number, the order, the customer, an invoice date, a due date
(30 days after the invoice date), an amount and a status (open, paid, cancelled). Create an invoice for a shipped
order, find it, and list the invoices of a customer.

## 15 Payments
Customers pay invoices, sometimes in parts: a payment has an invoice, a date and an amount. Record a payment; when
the payments reach the invoice amount, the invoice is paid. Show the payments of an invoice and the amount still due.

## 16 Overdue invoices
Finance needs the overdue invoices on a given date: open invoices whose due date has passed. All of them, those of one
customer, and those of customers in a given region, oldest due date first.

## 17 Shipments
Shipping a confirmed order creates a shipment: carrier name, tracking number, ship date, warehouse it left from.
Shipping takes the ordered quantities from that warehouse's stock (and fails if stock is short). Find the shipment of an
order and list the shipments of a warehouse between two dates.

## 18 Carriers
Keep our carriers: name, service level (standard, express), price per kg. List all and list by service level.
Shipments now refer to a carrier from this list.

## 19 Purchase orders
We buy from suppliers: a purchase order has a number, a supplier, an order date, an expected delivery date, a status
(draft, sent, received, cancelled) and lines (product, quantity, unit cost). Create one with its lines, find it, and
list the purchase orders of a supplier.

## 20 Receiving goods
When a purchase order is received into a warehouse, its quantities are added to that warehouse's stock and the order
becomes received. Record the receiving date.

## 21 Open purchase orders
Purchasing wants the purchase orders that are sent but not received: all, by supplier, and those whose expected
delivery date has passed on a given date.

## 22 Sales reps
Sales reps have a name, an email and a region. Customers can be assigned to a rep. List the reps, the reps of a
region, and the customers of a rep.

## 23 Rep performance
For a rep, show the number and the total value of the non-cancelled orders of their customers between two dates.

## 24 Customer addresses
A customer can have several addresses (billing, shipping), each with street, city, postal code and country, and one
default shipping address. Add, update and list a customer's addresses; set the default.

## 25 Returns
Customers return goods: a return has a number, the order, a date, a reason and lines (product, quantity). Returned
quantities go back into a warehouse's stock. Create a return, find it, and list the returns of an order and of a
customer.

## 26 Credit notes
An accepted return produces a credit note for the returned value at the order's prices: number, customer, date,
amount. List the credit notes of a customer and the total credited to a customer.

## 27 Promotions
Marketing runs promotions: a promotion has a code, a percentage discount, a start date, an end date and either a
product category it applies to or all products. List the promotions active on a given date and those of a category.

## 28 Applying promotions
When an order line is created, the best promotion active on the order date for the product's category (or all
products) is applied to its unit price. Record the promotion code on the line.

## 29 Price lists
Some customers have their own price list: per product, a special unit price valid between two dates. When ordering,
the customer's valid special price wins over the catalogue price (and promotions do not apply on top). Manage a
customer's price list and show the price a customer pays for a product on a date.

## 30 Product categories
Turn categories into a proper list: a category has a code, a name and an optional parent category. Products refer to a
category code. List categories, the children of a category, and the products in a category including its children.

## 31 Low stock alerts
Each product can have a reorder level per warehouse. List, for a warehouse, the products whose stock is at or below
their reorder level, and the same across all warehouses.

## 32 Supplier products
Record which suppliers deliver which product, with their lead time in days and their last unit cost. List the
suppliers of a product cheapest first, and the products of a supplier.

## 33 Customer credit limit
Customers have a credit limit. Confirming an order is refused when the customer's open invoice amount plus the new
order's value would exceed the limit. Show a customer's available credit.

## 34 Order notes
Staff can add notes to an order (author, date, text). List the notes of an order, newest first, and the notes written
by an author between two dates.

## 35 Customer activity
For a customer show: date of the first and the last order, number of orders, number of returns, total ordered value,
total paid, total credited.

## 36 Sales per region
Report the number of orders and their total value per region between two dates, regions sorted by value.

## 37 Sales per product
Report, between two dates, per product: units sold and revenue (non-cancelled orders), best sellers first. Also the
same for one category.

## 38 Sales per customer
Report, between two dates, per customer: number of orders and revenue, biggest customers first; and the top N
customers of a region.

## 39 Invoice aging
Finance wants the open invoice amount per customer in aging buckets on a given date: not due, 1–30 days overdue,
31–60, 61–90, more than 90.

## 40 Payments per period
List the payments received between two dates, all and per customer, and the total received per day.

## 41 Stock movements
Keep a log of every stock change (product, warehouse, date, quantity change, reason: shipment, receipt, return,
adjustment). List the movements of a product, of a warehouse, and of a product in a warehouse between two dates.

## 42 Stock adjustments
Warehouse staff count stock and correct it: record an adjustment (product, warehouse, date, counted quantity, reason);
the stock becomes the counted quantity and the difference is logged as a movement.

## 43 Stock value
Show the value of the stock (quantity times the product's last purchase unit cost) per warehouse and in total.

## 44 Back orders
When a confirmed order cannot be shipped because of short stock, it can be put on back order. List the back orders:
all, per customer, per product, oldest first.

## 45 Partial shipments
Allow shipping only some lines of an order (or part of a line's quantity). The order is shipped when everything is
shipped; until then it is partially shipped. Show what remains to be shipped for an order.

## 46 Delivery tracking
Shipments get a delivery date when the carrier reports delivery. List shipments in transit (shipped, not delivered)
for all carriers and for one carrier, and those in transit for more than N days on a given date.

## 47 Carrier performance
For each carrier, between two dates: number of shipments, average days to deliver, and number delivered late
(more than the carrier's promised days, a new field).

## 48 Customer segments
Classify customers by their revenue of the last 365 days before a given date: A (above 50,000), B (10,000–50,000),
C (below). List the customers of a segment and count customers per segment.

## 49 Dormant customers
List the active customers without an order in the N days before a given date, per region and in total.

## 50 Supplier performance
For each supplier between two dates: number of purchase orders received, average delay against the expected date,
and the share received late.

## 51 Purchase suggestions
For a warehouse, suggest purchase quantities: for each product at or below its reorder level, the quantity needed to
reach twice the reorder level, with the cheapest supplier for it.

## 52 Order search
Search orders by any combination of customer, status, region and date range; all criteria optional; newest first.

## 53 Invoice search
Search invoices by any combination of customer, status, due date range and minimum amount; oldest first.

## 54 Product price history
Keep the history of catalogue price changes (product, date, old price, new price). Show the history of a product and
the products whose price changed between two dates.

## 55 Currency
Customers can be invoiced in EUR, USD or GBP, with fixed exchange rates per date kept in a table. Invoices record their
currency and the amount in that currency; reports keep using EUR. Show a customer's open amount in their currency.

## 56 Tax rates
Add VAT: each country has a rate, valid from a date. Invoices show the net amount, the VAT amount and the gross amount,
using the rate of the customer's country on the invoice date.

## 57 Discount approvals
Order lines with a manual discount above 15% need approval by a sales manager: list the orders waiting for approval,
approve or reject them (with the approver and the date); unapproved orders cannot be confirmed.

## 58 Customer contacts
Customers have contact persons (name, role, email, phone). Add and list contacts of a customer, find contacts by role
across all customers, and find a contact by email.

## 59 Support tickets
Customers raise support tickets: number, customer, optional order, subject, priority (low, normal, high), status
(open, in progress, closed), dates. Create, change status, and list open tickets per customer and per priority.

## 60 Ticket reports
Report open tickets older than N days on a given date, tickets per status, and the average days to close per priority
between two dates.

## 61 Warehouse transfers
Move stock between warehouses: a transfer (number, from, to, date, lines) takes stock from one warehouse and adds it
to the other, logging both movements. List the transfers of a warehouse and those in a period.

## 62 Product bundles
A bundle product consists of other products in fixed quantities. Ordering a bundle ships its components: shipping
takes component stock. List bundles and the components of a bundle; the stock of a bundle is how many can be built.

## 63 Order cancellation reasons
Cancelling an order now requires a reason (from a fixed list: customer request, out of stock, credit, duplicate,
other). Report cancellations per reason and per customer between two dates.

## 64 Customer statements
Produce a customer statement for a period: opening balance, invoices, payments and credit notes in date order, and the
closing balance.

## 65 Sales targets
Each rep has a monthly sales target. Show per rep, for a month, the target, the actual revenue of their customers and
the percentage reached; and the reps below 80% for a month.

## 66 Commission
Reps earn 3% commission on paid invoices of their customers, 5% above their monthly target. Compute the commission of a
rep for a month and of all reps for a month.

## 67 Audit log
Record who changed what: for customers, orders and invoices, store the user, the date, the entity, its id and a short
description of the change. List the audit entries of an entity and those of a user between two dates.

## 68 Archiving
Orders delivered or cancelled more than two years before a given date can be archived: they are marked archived and no
longer appear in the open, search and per-customer order lists, but still count in the sales reports. Archive and
list archived orders.

## 69 Soft-deleting customers
Closing a customer no longer deletes anything: closed customers are hidden from customer lists, searches and segment
reports, but their orders and invoices remain. Reopen a customer.

## 70 Inactive products
Inactive products disappear from product search, price lists, purchase suggestions and low stock alerts, but stay in
historical orders and reports. Deactivate and reactivate a product.

## 71 Region managers
Regions get a manager (a rep). Show, for a region manager, the open orders, overdue invoices and open tickets of the
customers in their region.

## 72 Dashboard
One summary for a given date: number of open orders, value of open orders, overdue invoice amount, stock value, open
tickets, back orders, and orders waiting for discount approval.
