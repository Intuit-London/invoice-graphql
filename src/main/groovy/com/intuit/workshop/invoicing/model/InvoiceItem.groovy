package com.intuit.workshop.invoicing.model

import graphql.annotations.GraphQLField

class InvoiceItem {

    @GraphQLField
    public String id

    @GraphQLField
    public Invoice invoice

    @GraphQLField
    public String name

    @GraphQLField
    // TODO Amount
    public Integer price

}
