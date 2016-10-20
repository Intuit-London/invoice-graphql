package com.intuit.workshop.invoicing.graphql.schema.output

import com.intuit.workshop.invoicing.graphql.schema.base.BaseInvoice
import graphql.annotations.GraphQLField

class Invoice extends BaseInvoice {

    @GraphQLField
    public User user

    @GraphQLField
    public Customer customer

    @GraphQLField
    public List<InvoiceItem> items

}
