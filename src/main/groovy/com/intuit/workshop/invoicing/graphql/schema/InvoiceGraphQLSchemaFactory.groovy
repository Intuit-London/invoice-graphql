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
import graphql.schema.GraphQLInterfaceType
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import graphql.schema.TypeResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static graphql.schema.GraphQLInputObjectField.newInputObjectField

@Component
class InvoiceGraphQLSchemaFactory {

    @Autowired
    DataFetcher userQueryDataFetcher

    @Autowired
    DataFetcher nodeQueryDataFetcher

    @Autowired
    DataFetcher invoiceMutationDataFetcher

    GraphQLSchema build() {

        GraphQLObjectType outputUserType = GraphQLAnnotations.object(OutputUser);
        GraphQLObjectType outputInvoiceType = GraphQLAnnotations.object(OutputInvoice);
        GraphQLInputObjectType inputInvoiceType = GraphQLAnnotations.inputObject(GraphQLAnnotations.object(InputInvoice))

        Relay relay = new Relay();
        GraphQLInterfaceType nodeInterfaceType = relay.nodeInterface(
                new TypeResolver() {
                    public GraphQLObjectType getType(Object object) {
                        Relay.ResolvedGlobalId resolvedGlobalId = new Relay().fromGlobalId((String)object.id);
                        switch (resolvedGlobalId.type) {
                            case "/User":
                                return outputUserType
                            case "/Invoice":
                                return outputInvoiceType
                            default:
                                return null
                        }
                    }
                })

        GraphQLObjectType queryType = GraphQLObjectType.newObject()
                                                       .name("invoicingRootQuery")
                                                       .field(relay.nodeField(nodeInterfaceType, nodeQueryDataFetcher))
                                                       .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                                    .type(outputUserType)
                                                                                    .name("user")
                                                                                    .dataFetcher(userQueryDataFetcher)
                                                                                    .build())
                                                       .build();

        GraphQLInputObjectField inputInvoiceField = newInputObjectField()
                .name("invoice")
                .type(inputInvoiceType).build()

        GraphQLFieldDefinition invoiceOutputFieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
                                                                                    .name("invoice")
                                                                                    .type(outputInvoiceType).build()

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
