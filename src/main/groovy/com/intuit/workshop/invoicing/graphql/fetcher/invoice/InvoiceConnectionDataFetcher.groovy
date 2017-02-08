package com.intuit.workshop.invoicing.graphql.fetcher.invoice

import com.intuit.workshop.invoicing.domain.entity.Invoice
import com.intuit.workshop.invoicing.domain.entity.User
import com.intuit.workshop.invoicing.graphql.fetcher.PaginationUtil
import graphql.relay.ConnectionCursor
import graphql.relay.Edge
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class InvoiceConnectionDataFetcher implements DataFetcher {

    @Override
    Object get(DataFetchingEnvironment environment) {
        List<Invoice> invoices = ((User) environment.source).invoices ?: []
        List<Edge> edges = invoices.collect { new Edge(it, new ConnectionCursor(it.id)) }
        return PaginationUtil.paginate(edges, environment.arguments)
    }
}
