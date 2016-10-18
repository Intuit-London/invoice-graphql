package com.intuit.workshop.invoicing.graphql.fetcher

import com.intuit.workshop.invoicing.graphql.relay.GlobalIdHelper
import com.intuit.workshop.invoicing.graphql.repository.InvoiceRepository
import graphql.relay.Relay
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class NodeQueryDataFetcher implements DataFetcher {

    @Autowired
    InvoiceRepository repository

    @Override
    Object get(DataFetchingEnvironment environment) {
        String globalId = (String)environment.arguments.id
        Relay.ResolvedGlobalId resolvedGlobalId = GlobalIdHelper.fromId(globalId)
        switch (resolvedGlobalId.type) {
            case "/User":
                return repository.getUserById(globalId)
            case "/Invoice":
                return repository.getInvoiceById(globalId)
            default:
                return null
        }
    }
}
