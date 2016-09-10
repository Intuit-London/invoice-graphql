package com.intuit.workshop.invoicing.model

import graphql.annotations.GraphQLField

class Invoice {

    @GraphQLField
    public String id

    @GraphQLField
    public User user

    @GraphQLField
    public Long number

    @GraphQLField
    public Customer customer

    @GraphQLField
    // TODO Date
    public String creationDate

    @GraphQLField
    // TODO Date
    public String paymentDate

    @GraphQLField
    public Boolean paid

    @GraphQLField
    public List<InvoiceItem> items

    @GraphQLField
    // TODO Amount
    public Integer totalAmount

}
