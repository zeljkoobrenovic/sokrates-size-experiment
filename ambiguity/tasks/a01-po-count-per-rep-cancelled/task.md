# Purchase orders per sales rep include cancelled purchase orders

The report that shows, for each sales rep, how many purchase orders were placed in a period for the regions their customers are in, also counts purchase orders that were cancelled. A cancelled purchase order never reaches the warehouse, so it should not make a rep look busier than they are. All the other per-rep counts we have (new customers, orders, invoices, shipments, returns) already leave cancelled records out, and the purchase-order reports per quarter and the latest purchase orders do too.

Example: Anna's customers are in the North, Bob's in the South. In January the North warehouse placed one sent and one received purchase order, the South warehouse one sent and two cancelled ones. Today the report shows Bob 3, Anna 2; it should show Anna 2, Bob 1. Reps without purchase orders in the period should still be listed with 0, highest count first as before.
