package com.intuit.workshop.invoicing.graphql.schema.input

import com.intuit.workshop.invoicing.graphql.schema.base.BaseInvoice
import graphql.annotations.GraphQLField

class InputInvoice extends BaseInvoice {

    @GraphQLField
    public InputUser user

    @GraphQLField
    public InputCustomer customer

}
