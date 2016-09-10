package com.intuit.workshop.invoicing.graphql.fetcher

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class InvoiceMutationDataFetcher implements DataFetcher {

    @Override
    Object get(DataFetchingEnvironment environment) {
        return null
    }
}
