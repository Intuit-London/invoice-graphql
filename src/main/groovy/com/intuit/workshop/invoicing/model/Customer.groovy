package com.intuit.workshop.invoicing.model

import graphql.annotations.GraphQLField

class Customer {

    @GraphQLField
    public String id

    @GraphQLField
    public String businessName

    @GraphQLField
    public List<Invoice> invoices
}
