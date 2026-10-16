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

# Second batch (used only if the first 72 do not reach the entry criteria)

## 73 Orders per sales channel
Orders now record the channel they came from (web, phone, email, field sales). List the orders of a channel between
two dates, and report the number of orders and revenue per channel.

## 74 Open orders per warehouse
For each warehouse, list the open orders whose lines it can fully ship from its own stock, and those it cannot.

## 75 Customer order history export
For a customer, list every order with its date, status, number of lines and total, oldest first, and the same for the
orders of all customers of a region.

## 76 Invoices per sales rep
List the open invoices of the customers of a sales rep, the overdue ones, and the paid ones between two dates.

## 77 Shipments per customer
List the shipments of a customer between two dates, those still in transit, and those delivered late.

## 78 Returns per product
List the returns containing a product between two dates, and report the returned quantity per product, most returned
first, overall and for one category.

## 79 Returns per reason
Report the number of returns and the returned value per reason between two dates, overall and per region.

## 80 Purchase orders per warehouse
Purchase orders now name the warehouse they will be received into. List the open purchase orders of a warehouse and
those overdue on a given date.

## 81 Supplier spending
Report, between two dates, the value of received purchase orders per supplier, biggest first, and per country.

## 82 Product margin
Show, per product between two dates, the revenue, the purchase cost of the units sold (at the last purchase cost) and
the margin; the products with the lowest margin first.

## 83 Category margin
The same margin report per category, and the categories whose margin fell below a given percentage.

## 84 Customers per region report
Per region: number of active customers, customers on hold, new customers between two dates, and dormant customers on
a given date.

## 85 Overdue invoices per sales rep
For each rep, the number and amount of overdue invoices of their customers on a given date, biggest amount first.

## 86 Back orders per warehouse
List the back orders that a given warehouse could ship now from its stock, and the back orders per product category.

## 87 Promotion usage
Report per promotion between two dates: the number of order lines it was applied to, the discount given and the
revenue of those lines; the unused promotions.

## 88 Price list usage
Report per customer with a price list, between two dates, the revenue at special prices and what it would have been at
catalogue prices.

## 89 Credit notes per period
List the credit notes between two dates, all and per region, and report the credited amount per reason of the
underlying returns.

## 90 Payments per sales rep
List the payments received from the customers of a sales rep between two dates, and the total per rep.

## 91 Tickets per product
Tickets can name a product. List the open tickets about a product, and report tickets per product between two dates.

## 92 Tickets per region
List the open tickets of the customers of a region, per priority, and the high-priority tickets older than N days on a
given date.

## 93 Stock per category
Show the stock quantity and value per category, per warehouse and in total, and the categories with no stock in a
warehouse.

## 94 Slow-moving stock
List the products with stock but no shipment in the N days before a given date, per warehouse and in total, by stock
value.

## 95 Order line search
Search order lines by any combination of product, category, customer, date range and minimum quantity, newest first.

## 96 Shipment search
Search shipments by any combination of carrier, warehouse, customer, status (in transit or delivered) and date range.

## 97 Purchase order search
Search purchase orders by any combination of supplier, warehouse, status and expected date range.

## 98 Return search
Search returns by any combination of customer, product, reason and date range.

## 99 Monthly sales report
For a year, report per month: number of orders, revenue, number of new customers, number of returns and credited
amount.

## 100 Year-over-year comparison
For a month, compare revenue, orders and average order value with the same month of the previous year, overall and
per region.

# Third batch (used only if the first 100 do not reach the entry criteria)

## 101 Orders per carrier
List the orders shipped with a given carrier between two dates, and report per carrier the number of orders and their
value.

## 102 Orders with returns
List the orders that have at least one return, per customer and between two dates, with the returned share of their
value.

## 103 Large orders
List the orders above a given value between two dates, per region, biggest first, and the customers who placed them.

## 104 Small orders
List the orders below a given value between two dates, per customer, and the share of such orders per region.

## 105 Order lead time
For delivered orders between two dates, report the average days from order to delivery, per region and per carrier.

## 106 Cancelled orders list
List the cancelled orders between two dates, per customer and per region, newest first.

## 107 Draft orders cleanup list
List the draft orders older than N days on a given date, per customer and per sales rep.

## 108 Orders per product
List the non-cancelled orders that contain a product between two dates, newest first, and per customer.

## 109 Orders per category
List the non-cancelled orders that contain a product of a category between two dates, and the revenue per category
per month.

## 110 Repeat customers
List the customers with at least N orders between two dates, per region, most orders first.

## 111 First-time customers
List the customers whose first order falls between two dates, per region and per sales rep.

## 112 Customers without invoices
List the active customers that have orders but no invoice between two dates.

## 113 Customers with open credit
List the customers with credit notes not yet used against an invoice, and the total open credit per region.

## 114 Customers over credit limit
List the customers whose open invoice amount exceeds their credit limit on a given date, per region and per sales rep.

## 115 Customers per segment and region
Count and list the customers of each segment per region on a given date.

## 116 Invoices per region
List the invoices of a region between two dates, the open ones and the paid ones, and the total per status.

## 117 Paid late invoices
List the invoices paid after their due date between two dates, per customer, with the days late.

## 118 Partially paid invoices
List the open invoices with at least one payment, per customer and per region, with the amount still due.

## 119 Invoices per currency
List the open invoices per currency, with their amount in that currency and in EUR, per customer.

## 120 Large invoices
List the invoices above a given amount between two dates, per region, biggest first.

## 121 Payments per region
List the payments received from customers of a region between two dates, and the total per month.

## 122 Unapplied payments
List the payments that exceed the amount due of their invoice (overpayments), per customer.

## 123 Shipments per region
List the shipments to customers of a region between two dates, those in transit and those delivered late.

## 124 Shipments per product
List the shipments containing a product between two dates, and the shipped quantity per warehouse.

## 125 Late shipments per warehouse
List the shipments delivered late per warehouse between two dates, and the share delivered late per warehouse.

## 126 Shipment weight
Products get a weight. Report the shipped weight per carrier and per warehouse between two dates.

## 127 Returns per customer
List the returns of a customer between two dates and the returned value; the customers with the most returns first.

## 128 Returns per warehouse
List the returns received into a warehouse between two dates, and the returned quantity per product there.

## 129 Purchase orders per product
List the purchase orders containing a product between two dates, and the purchased quantity per supplier.

## 130 Purchase orders per category
List the purchase orders containing products of a category between two dates, and the spend per category.

## 131 Late purchase orders per warehouse
List the purchase orders received late into a warehouse between two dates, with the days late.

## 132 Supplier products per category
List the supplier products of a category, cheapest supplier first, and the categories with a single supplier.

## 133 Stock per supplier
Show the stock quantity and value of the products a supplier delivers, per warehouse.

## 134 Stock movements per reason
Report the stock movements per reason between two dates, per warehouse and per product category.

## 135 Stock movements per product category
List the stock movements of the products of a category between two dates, per warehouse.

## 136 Negative adjustments
List the stock adjustments that lowered the stock between two dates, per warehouse, biggest loss first.

## 137 Transfers per product
List the warehouse transfers containing a product between two dates, and the transferred quantity per route.

## 138 Promotions per region
Promotions can be limited to a region. List the promotions active in a region on a given date.

## 139 Promotions per customer
List the order lines of a customer that got a promotion between two dates, and the discount given to that customer.

## 140 Price list per category
List a customer's special prices for the products of a category, valid on a given date.

## 141 Expiring price lists
List the special prices that expire in the N days after a given date, per customer.

## 142 Tickets per sales rep
List the open tickets of the customers of a sales rep, per priority, oldest first.

## 143 Tickets per order
List the tickets that refer to an order, per customer, and the orders with more than one ticket.

## 144 Closed tickets
List the tickets closed between two dates, per customer and per priority, with the days to close.

## 145 Contacts per region
List the contact persons of the customers of a region, per role.

## 146 Notes per customer
List the order notes of all orders of a customer, newest first, and those of an author for a customer.

## 147 Audit entries per entity type
List the audit entries of one entity type (customer, order, invoice) between two dates, newest first.

## 148 Archived orders per customer
List the archived orders of a customer and of a region, and the archived revenue per year.

## 149 Sales rep customers report
For a sales rep: the customers, their segment, their last order date and their open invoice amount.

## 150 Region report
For a region and a period: customers, orders, revenue, open invoices, overdue amount, shipments, returns and open
tickets.
