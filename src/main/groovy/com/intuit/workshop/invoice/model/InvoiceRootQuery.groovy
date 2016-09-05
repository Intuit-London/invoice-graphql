package com.intuit.workshop.invoice.model

import com.intuit.workshop.invoice.fetcher.InvoiceDataFetcher
import graphql.annotations.GraphQLDataFetcher
import graphql.annotations.GraphQLField

class InvoiceRootQuery {

    @GraphQLField
    @GraphQLDataFetcher(InvoiceDataFetcher.class)
    public Invoice invoice

}
