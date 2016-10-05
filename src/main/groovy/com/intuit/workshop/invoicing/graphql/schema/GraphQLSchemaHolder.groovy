package com.intuit.workshop.invoicing.graphql.schema

import graphql.schema.GraphQLSchema
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class GraphQLSchemaHolder {

    GraphQLSchema graphQLSchema

    @Autowired
    InvoiceGraphQLSchemaFactory invoiceGraphQLSchemaFactory

    @PostConstruct
    void buildGraphQLSchema() {
        graphQLSchema = invoiceGraphQLSchemaFactory.build()
    }

}
