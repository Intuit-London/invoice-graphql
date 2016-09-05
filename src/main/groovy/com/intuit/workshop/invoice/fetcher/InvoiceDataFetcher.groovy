package com.intuit.workshop.invoice.fetcher

import com.intuit.workshop.invoice.model.Invoice
import com.intuit.workshop.invoice.model.InvoiceItem
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment

class InvoiceDataFetcher implements DataFetcher {

    @Override
    Object get(DataFetchingEnvironment environment) {
        Invoice invoice = new Invoice(id: "100", number: 5, items: [])
        invoice.items << new InvoiceItem(name: "Bags", price: 100, invoice: invoice)
        return invoice
    }
}
