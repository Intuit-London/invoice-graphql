package com.intuit.workshop.invoice.model

import graphql.annotations.GraphQLField

class InvoiceItem {

    @GraphQLField
    public Invoice invoice

    @GraphQLField
    public String id

    @GraphQLField
    public String name

    @GraphQLField
    public Integer price

}
