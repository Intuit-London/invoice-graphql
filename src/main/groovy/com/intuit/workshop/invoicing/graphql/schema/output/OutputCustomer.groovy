package com.intuit.workshop.invoicing.graphql.schema.output

import com.intuit.workshop.invoicing.graphql.schema.base.BaseCustomer
import graphql.annotations.GraphQLField

class OutputCustomer extends BaseCustomer {

    @GraphQLField
    public List<OutputInvoice> invoices
}
