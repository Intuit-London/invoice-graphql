package com.intuit.workshop.invoicing.graphql.schema.output

import com.intuit.workshop.invoicing.graphql.schema.base.BaseInvoiceItem
import graphql.annotations.GraphQLField

class OutputInvoiceItem extends BaseInvoiceItem {

    @GraphQLField
    public OutputInvoice invoice

}
