package com.intuit.workshop.invoicing.graphql.schema

import com.intuit.workshop.invoicing.domain.entity.Invoice
import com.intuit.workshop.invoicing.domain.entity.InvoiceItem

class RelayMutation {

    String clientMutationId

    Invoice invoice

    InvoiceItem invoiceItem
}
