package com.intuit.workshop.invoicing.graphql.schema.output

import com.intuit.workshop.invoicing.graphql.schema.base.BaseInvoiceItem
import graphql.annotations.GraphQLField

class InvoiceItem extends BaseInvoiceItem {

    @GraphQLField
    public Invoice invoice

}
