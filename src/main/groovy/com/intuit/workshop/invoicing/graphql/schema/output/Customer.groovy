package com.intuit.workshop.invoicing.graphql.schema.output

import com.intuit.workshop.invoicing.graphql.schema.base.BaseCustomer
import graphql.annotations.GraphQLField

class Customer extends BaseCustomer {

    @GraphQLField
    public List<Invoice> invoices
}
