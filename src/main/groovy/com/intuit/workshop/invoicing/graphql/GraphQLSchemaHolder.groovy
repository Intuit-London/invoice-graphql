package com.intuit.workshop.invoicing.graphql

import com.intuit.workshop.invoicing.model.Invoice
import com.intuit.workshop.invoicing.model.User
import graphql.annotations.GraphQLAnnotations
import graphql.relay.Relay
import graphql.schema.DataFetcher
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLInputObjectField
import graphql.schema.GraphQLInputObjectType
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
class GraphQLSchemaHolder {

    GraphQLSchema graphQLSchema

    @Autowired
    DataFetcher rootQueryDataFetcher

    @Autowired
    DataFetcher invoiceMutationDataFetcher

    @PostConstruct
    void buildGraphQLSchema() {

        GraphQLObjectType userType = GraphQLAnnotations.object(User);
        GraphQLObjectType invoiceType = GraphQLAnnotations.object(Invoice);

        GraphQLObjectType queryType = GraphQLObjectType.newObject()
                                                       .name("invoicingRootQuery")
                                                       .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                                    .type(userType)
                                                                                    .name("user")
                                                                                    .dataFetcher(rootQueryDataFetcher)
                                                                                    .build())
                                                       .build();

        GraphQLInputObjectType invoiceInputType = GraphQLAnnotations.inputObject(invoiceType)
        GraphQLInputObjectField inputInvoiceField = GraphQLInputObjectField.newInputObjectField()
                                                                           .name("invoice")
                                                                           .type(invoiceInputType).build()

        GraphQLFieldDefinition invoiceOutputFieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
                                                                                    .name("invoice")
                                                                                    .type(invoiceType).build()

        Relay relay = new Relay()
        GraphQLFieldDefinition invoiceMutationFieldDefinition = relay.mutationWithClientMutationId("Invoice", "updateInvoice", [inputInvoiceField],
                [invoiceOutputFieldDefinition], invoiceMutationDataFetcher)

        GraphQLObjectType mutationType = GraphQLObjectType.newObject()
                                                          .name("invoicingMutation")
                                                          .field(invoiceMutationFieldDefinition)
                                                          .build();

        graphQLSchema = GraphQLSchema.newSchema()
                              .query(queryType)
                              .mutation(mutationType)
                              .build();

    }


}
