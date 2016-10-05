package com.intuit.workshop.invoicing.graphql.schema.output

import com.intuit.workshop.invoicing.graphql.schema.base.BaseUser
import graphql.annotations.GraphQLField

class OutputUser extends BaseUser {

    @GraphQLField
    public List<OutputInvoice> invoices

}
