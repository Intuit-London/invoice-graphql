package com.intuit.workshop.invoicing.graphql.fetcher

import com.intuit.workshop.invoicing.domain.entity.id.EntityType
import com.intuit.workshop.invoicing.domain.entity.id.GlobalIdHelper
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

    private final Map<EntityType, Closure> ACTIONS = [
            (EntityType.USER): { String id -> repository.getUserById(id) },
            (EntityType.CUSTOMER): { String id -> repository.getCustomerById(id) },
            (EntityType.INVOICE): { String id -> repository.getInvoiceById(id) },
            (EntityType.INVOICE_ITEM): { String id -> repository.getInvoiceItemById(id) },
    ]

    @Override
    Object get(DataFetchingEnvironment environment) {
        String globalId = (String)environment.arguments.id
        Relay.ResolvedGlobalId resolvedGlobalId = GlobalIdHelper.fromId(globalId)
        return ACTIONS[EntityType.fromType(resolvedGlobalId.type)]?.call(globalId)
    }
}
