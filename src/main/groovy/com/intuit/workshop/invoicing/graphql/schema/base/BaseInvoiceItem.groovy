package com.intuit.workshop.invoicing.graphql.schema.base

import graphql.annotations.GraphQLField

class BaseInvoiceItem implements Entity {

    @GraphQLField
    public String id

    @GraphQLField
    public String name

    @GraphQLField
    // TODO Amount
    public Integer price
}
