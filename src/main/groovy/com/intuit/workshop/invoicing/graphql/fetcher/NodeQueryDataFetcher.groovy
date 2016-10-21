package com.intuit.workshop.invoicing.graphql.fetcher

import com.intuit.workshop.invoicing.graphql.relay.GlobalIdHelper
import com.intuit.workshop.invoicing.domain.repository.InvoiceRepository
import graphql.relay.Relay
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class NodeQueryDataFetcher implements DataFetcher {

    @Autowired
    InvoiceRepository repository

    private final Map<String, Closure> ACTIONS = [
            "/User": { String id -> repository.getUserById(id) },
            "/Customer": { String id -> repository.getCustomerById(id) },
            "/Invoice": { String id -> repository.getInvoiceById(id) },
            "/InvoiceItem": { String id -> repository.getInvoiceItemById(id) },
    ]

    @Override
    Object get(DataFetchingEnvironment environment) {
        String globalId = (String)environment.arguments.id
        Relay.ResolvedGlobalId resolvedGlobalId = GlobalIdHelper.fromId(globalId)
        return ACTIONS[resolvedGlobalId.type]?.call(globalId)
    }
}
