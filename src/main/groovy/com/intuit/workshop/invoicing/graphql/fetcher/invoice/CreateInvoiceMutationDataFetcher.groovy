package com.intuit.workshop.invoicing.graphql.fetcher.invoice

import com.intuit.workshop.invoicing.domain.entity.Invoice
import com.intuit.workshop.invoicing.domain.service.InvoiceService
import com.intuit.workshop.invoicing.graphql.schema.RelayMutation
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.Assert

@Component
class CreateInvoiceMutationDataFetcher implements DataFetcher {

    @Autowired
    InvoiceService service

    @Override
    Object get(DataFetchingEnvironment environment) {
        Map input = (Map)environment.arguments.input
        Assert.notNull(input, "Input can not be null")
        Invoice invoice = service.createInvoice(input?.invoice ?: [:])
        return new RelayMutation(clientMutationId: input.clientMutationId, invoice: invoice)
    }

}
