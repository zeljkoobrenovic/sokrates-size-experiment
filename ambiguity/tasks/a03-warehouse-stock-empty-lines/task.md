# Warehouse stock list shows products with nothing left

The stock list of a warehouse is meant to show the products stocked there with their quantities, by product name. Once a product has been taken out completely (or its count was set to zero), it keeps showing up in that list with quantity 0, so the list grows with dead lines that warehouse staff have to skip. The other stock overviews (stock per category, per category and warehouse, slow-moving stock) already leave products with no stock out.

Example: warehouse WH-1 holds 10 bolts, 4 nuts are taken out until none are left, and the screw count is set to 0. The stock list of WH-1 should show only "Bolt 10". Asking for the quantity of nuts in WH-1 should still answer 0.
