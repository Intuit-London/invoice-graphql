package com.intuit.workshop.invoicing.graphql.schema

import com.intuit.workshop.invoicing.graphql.schema.input.InputInvoice
import com.intuit.workshop.invoicing.graphql.schema.output.OutputInvoice
import com.intuit.workshop.invoicing.graphql.schema.output.OutputUser
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

import static graphql.schema.GraphQLInputObjectField.newInputObjectField

@Component
class InvoiceGraphQLSchemaFactory {

    @Autowired
    DataFetcher rootQueryDataFetcher

    @Autowired
    DataFetcher invoiceMutationDataFetcher

    GraphQLSchema build() {

        GraphQLObjectType outputUserType = GraphQLAnnotations.object(OutputUser);
        GraphQLObjectType outputInvoiceType = GraphQLAnnotations.object(OutputInvoice);
        GraphQLInputObjectType inputInvoiceType = GraphQLAnnotations.inputObject(GraphQLAnnotations.object(InputInvoice))

        GraphQLObjectType queryType = GraphQLObjectType.newObject()
                                                       .name("invoicingRootQuery")
                                                       .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                                    .type(outputUserType)
                                                                                    .name("user")
                                                                                    .dataFetcher(rootQueryDataFetcher)
                                                                                    .build())
                                                       .build();

        GraphQLInputObjectField inputInvoiceField = newInputObjectField()
                .name("invoice")
                .type(inputInvoiceType).build()

        GraphQLFieldDefinition invoiceOutputFieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
                                                                                    .name("invoice")
                                                                                    .type(outputInvoiceType).build()

        Relay relay = new Relay();
        GraphQLFieldDefinition invoiceMutationFieldDefinition = relay.mutationWithClientMutationId("Invoice", "updateInvoice", [inputInvoiceField],
                [invoiceOutputFieldDefinition], invoiceMutationDataFetcher)

        GraphQLObjectType mutationType = GraphQLObjectType.newObject()
                                                          .name("invoicingMutation")
                                                          .field(invoiceMutationFieldDefinition)
                                                          .build();

        return GraphQLSchema.newSchema()
                                     .query(queryType)
                                     .mutation(mutationType)
                                     .build();
    }
}
