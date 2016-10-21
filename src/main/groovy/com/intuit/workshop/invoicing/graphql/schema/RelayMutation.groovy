package com.intuit.workshop.invoicing.graphql.schema

import com.intuit.workshop.invoicing.domain.entity.Invoice

class RelayMutation {

    String clientMutationId

    Invoice invoice
}
