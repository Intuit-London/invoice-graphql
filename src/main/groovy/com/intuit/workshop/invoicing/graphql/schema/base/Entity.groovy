package com.intuit.workshop.invoicing.graphql.schema.base

import graphql.annotations.GraphQLField

public interface Entity {

    @GraphQLField
    String id

}
