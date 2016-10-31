package com.intuit.workshop.invoicing.graphql.fetcher

import com.intuit.workshop.invoicing.domain.repository.InvoiceRepository
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
        String globalId = (String)environment.arguments.id
        if (globalId) {
            return [repository.getUserById(globalId)]
        }
        return repository.findUsers()
    }
}
