package com.intuit.workshop.invoice.model

import graphql.annotations.GraphQLField

class Invoice {

    @GraphQLField
    public String id

    @GraphQLField
    public Long number

    @GraphQLField
    public Date creationDate

    @GraphQLField
    public List<InvoiceItem> items

}
