package com.intuit.workshop.invoicing.graphql.schema.base

import graphql.annotations.GraphQLField

class BaseCustomer {

    @GraphQLField
    public String id

    @GraphQLField
    public String businessName
}
