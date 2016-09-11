package com.intuit.workshop.invoicing.graphql.payload

import com.intuit.workshop.invoicing.model.Invoice

class RelayMutationResponse {

    String clientMutationId

    Invoice invoice
}
