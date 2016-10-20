package com.intuit.workshop.invoicing.graphql.schema.output

import com.intuit.workshop.invoicing.graphql.schema.base.BaseUser
import graphql.annotations.GraphQLField

class User extends BaseUser {

    @GraphQLField
    public List<Invoice> invoices

}
