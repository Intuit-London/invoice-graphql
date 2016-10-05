package com.intuit.workshop.invoicing.graphql.schema.output

import com.intuit.workshop.invoicing.graphql.schema.base.BaseInvoice
import graphql.annotations.GraphQLField

class OutputInvoice extends BaseInvoice {

    @GraphQLField
    public OutputUser user

    @GraphQLField
    public OutputCustomer customer

    @GraphQLField
    public List<OutputInvoiceItem> items

}
