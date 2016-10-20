package com.intuit.workshop.invoicing.graphql.schema

import com.intuit.workshop.invoicing.graphql.schema.input.InputInvoice
import graphql.Scalars
import graphql.annotations.GraphQLAnnotations
import graphql.relay.Relay
import graphql.schema.DataFetcher
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLInputObjectField
import graphql.schema.GraphQLInputObjectType
import graphql.schema.GraphQLInterfaceType
import graphql.schema.GraphQLList
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import graphql.schema.GraphQLTypeReference
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

        GraphQLObjectType OutputUserType = null
        GraphQLObjectType OutputInvoiceType = null
        GraphQLObjectType OutputInvoiceItemType = null
        GraphQLObjectType OutputCustomerType = null

        GraphQLInputObjectType inputInvoiceType = GraphQLAnnotations.inputObject(GraphQLAnnotations.object(InputInvoice))

        Relay relay = new Relay();
        GraphQLInterfaceType NodeInterfaceType = relay.nodeInterface(
                new TypeResolver() {
                    public GraphQLObjectType getType(Object object) {
                        Relay.ResolvedGlobalId resolvedGlobalId = new Relay().fromGlobalId((String) object.id);
                        switch (resolvedGlobalId.type) {
                            case "/User":
                                return OutputUserType
                            case "/Invoice":
                                return OutputInvoiceType
                            case "/InvoiceItem":
                                return OutputInvoiceItemType
                            case "/Customer":
                                return OutputCustomerType
                            default:
                                return null
                        }
                    }
                })


        OutputCustomerType = GraphQLObjectType.newObject()
                                              .name("Customer")
                                              .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                           .name("id")
                                                                           .type(new GraphQLNonNull(Scalars.GraphQLID))
                                                                           .build())

                                              .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                           .name("businessName")
                                                                           .type(Scalars.GraphQLString)
                                                                           .build())

                                              .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                           .name("invoices")
                                                                           .type(new GraphQLList(new GraphQLTypeReference("Invoice")))
                                                                           .build())

                                              .withInterface(NodeInterfaceType)
                                              .build()

        OutputInvoiceItemType = GraphQLObjectType.newObject()
                                                 .name("InvoiceItem")
                                                 .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                              .name("id")
                                                                              .type(new GraphQLNonNull(Scalars.GraphQLID))
                                                                              .build())

                                                 .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                              .name("name")
                                                                              .type(Scalars.GraphQLString)
                                                                              .build())

                                                 .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                              .name("price")
                                                                              .type(Scalars.GraphQLBigDecimal)
                                                                              .build())

                                                 .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                              .name("invoice")
                                                                              .type(new GraphQLTypeReference("Invoice"))
                                                                              .build())

                                                 .withInterface(NodeInterfaceType)
                                                 .build()

        OutputInvoiceType = GraphQLObjectType.newObject()
                                             .name("Invoice")
                                             .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                          .name("id")
                                                                          .type(new GraphQLNonNull(Scalars.GraphQLID))
                                                                          .build())

                                             .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                          .name("number")
                                                                          .type(Scalars.GraphQLLong)
                                                                          .build())

                                             .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                          .name("creationDate")
                                                                          .type(Scalars.GraphQLString)
                                                                          .build())

                                             .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                          .name("paymentDate")
                                                                          .type(Scalars.GraphQLString)
                                                                          .build())

                                             .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                          .name("paid")
                                                                          .type(Scalars.GraphQLBoolean)
                                                                          .build())

                                             .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                          .name("totalAmount")
                                                                          .type(Scalars.GraphQLBigDecimal)
                                                                          .build())

                                             .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                          .name("user")
                                                                          .type(new GraphQLTypeReference("User"))
                                                                          .build())

                                             .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                          .name("customer")
                                                                          .type(OutputCustomerType)
                                                                          .build())

                                             .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                          .name("items")
                                                                          .type(new GraphQLList(OutputInvoiceItemType))
                                                                          .build())

                                             .withInterface(NodeInterfaceType)
                                             .build()


        OutputUserType = GraphQLObjectType.newObject()
                                          .name("User")
                                          .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                       .name("id")
                                                                       .type(new GraphQLNonNull(Scalars.GraphQLID))
                                                                       .build())

                                          .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                       .name("firstName")
                                                                       .type(Scalars.GraphQLString)
                                                                       .build())

                                          .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                       .name("lastName")
                                                                       .type(Scalars.GraphQLString)
                                                                       .build())

                                          .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                       .name("invoices")
                                                                       .type(new GraphQLList(OutputInvoiceType))
                                                                       .build())

                                          .withInterface(NodeInterfaceType)
                                          .build()

        GraphQLObjectType queryType = GraphQLObjectType.newObject()
                                                       .name("invoicingRootQuery")
                                                       .field(relay.nodeField(NodeInterfaceType, nodeQueryDataFetcher))
                                                       .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                                    .type(new GraphQLList(OutputUserType))
                                                                                    .name("users")
                                                                                    .dataFetcher(userQueryDataFetcher)
                                                                                    .build())
                                                       .build();

        GraphQLInputObjectField inputInvoiceField = newInputObjectField()
                .name("invoice")
                .type(inputInvoiceType).build()

        GraphQLFieldDefinition invoiceOutputFieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
                                                                                    .name("invoice")
                                                                                    .type(OutputInvoiceType).build()

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
