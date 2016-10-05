package com.intuit.workshop.invoicing.graphql.schema.base

import graphql.annotations.GraphQLField

class BaseUser {

    @GraphQLField
    public String id

    @GraphQLField
    public String firstName

    @GraphQLField
    public String lastName
}
