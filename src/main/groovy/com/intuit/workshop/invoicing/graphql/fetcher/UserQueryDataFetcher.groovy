package com.intuit.workshop.invoicing.graphql.fetcher

import com.intuit.workshop.invoicing.graphql.repository.InvoiceRepository
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserQueryDataFetcher implements DataFetcher {

    @Autowired
    InvoiceRepository repository

    @Override
    Object get(DataFetchingEnvironment environment) {
        return repository.findUsers()
    }
}
