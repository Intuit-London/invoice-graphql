package com.intuit.workshop.invoicing.model

import graphql.annotations.GraphQLField

class User {

    @GraphQLField
    public String id

    @GraphQLField
    public String firstName

    @GraphQLField
    public String lastName

    @GraphQLField
    public List<Invoice> invoices

}
