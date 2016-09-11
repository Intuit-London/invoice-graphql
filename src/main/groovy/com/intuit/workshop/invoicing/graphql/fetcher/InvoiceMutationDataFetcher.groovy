package com.intuit.workshop.invoicing.graphql.fetcher

import com.intuit.workshop.invoicing.graphql.fetcher.util.StaticModelBuilder
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class InvoiceMutationDataFetcher implements DataFetcher {

    @Override
    Object get(DataFetchingEnvironment environment) {
        return StaticModelBuilder.buildStaticModel()
    }

}
