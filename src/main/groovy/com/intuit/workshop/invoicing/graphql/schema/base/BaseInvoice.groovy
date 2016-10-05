package com.intuit.workshop.invoicing.graphql.schema.base

import graphql.annotations.GraphQLField

class BaseInvoice {

    @GraphQLField
    public String id

    @GraphQLField
    public Long number

    @GraphQLField
    // TODO Date
    public String creationDate

    @GraphQLField
    // TODO Date
    public String paymentDate

    @GraphQLField
    public Boolean paid

    @GraphQLField
    // TODO Amount
    public Integer totalAmount

}

