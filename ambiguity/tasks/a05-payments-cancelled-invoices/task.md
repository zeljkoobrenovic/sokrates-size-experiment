# Payment lists include payments on cancelled invoices

Finance noticed that payments booked against invoices that were later cancelled still appear in the lists of payments received in a period: the list of all payments, the payments of one customer, the payments of a region and the payments of a sales rep's customers. Those amounts are being refunded or moved, so they inflate what we report as received. The customer statement already leaves them out; every payment list for a period should do the same.

Example: customer Alpha (region North, sales rep Anna) paid 10.00 on invoice I-1 on 10 June and 20.00 on invoice I-2 on 11 June, and I-2 was then cancelled. For 5–15 June, all four lists (all payments, Alpha's, North's, Anna's) should show only the payment on I-1.
