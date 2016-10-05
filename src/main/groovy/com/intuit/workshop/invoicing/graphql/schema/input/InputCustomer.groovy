package com.intuit.workshop.invoicing.graphql.schema.input

import com.intuit.workshop.invoicing.graphql.schema.base.BaseCustomer
import graphql.annotations.GraphQLField

class InputCustomer extends BaseCustomer {

    @GraphQLField
    public List<InputInvoice> invoices
}
