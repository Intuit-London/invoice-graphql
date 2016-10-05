package com.intuit.workshop.invoicing.graphql.schema.input

import com.intuit.workshop.invoicing.graphql.schema.base.BaseUser
import graphql.annotations.GraphQLField

class InputUser extends BaseUser {

    @GraphQLField
    public List<InputInvoice> invoices
}
