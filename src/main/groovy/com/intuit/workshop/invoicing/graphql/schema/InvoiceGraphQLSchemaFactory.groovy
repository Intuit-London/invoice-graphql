package com.intuit.workshop.invoicing.graphql.schema

import com.intuit.workshop.invoicing.domain.entity.id.EntityType
import graphql.Scalars
import graphql.relay.Relay
import graphql.schema.DataFetcher
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLInputObjectField
import graphql.schema.GraphQLInputObjectType
import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLInterfaceType
import graphql.schema.GraphQLList
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import graphql.schema.GraphQLTypeReference
import graphql.schema.TypeResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class InvoiceGraphQLSchemaFactory {

    @Autowired
    DataFetcher userQueryDataFetcher

    @Autowired
    DataFetcher nodeQueryDataFetcher

    @Autowired
    DataFetcher createInvoiceMutationDataFetcher

    @Autowired
    DataFetcher updateInvoiceMutationDataFetcher

    GraphQLSchema build() {

        GraphQLObjectType OutputUserType = null
        GraphQLObjectType OutputInvoiceType = null
        GraphQLObjectType OutputInvoiceItemType = null
        GraphQLObjectType OutputCustomerType = null

        Relay relay = new Relay();
        GraphQLInterfaceType NodeInterfaceType = relay.nodeInterface(
                new TypeResolver() {
                    public GraphQLObjectType getType(Object object) {
                        Relay.ResolvedGlobalId resolvedGlobalId = new Relay().fromGlobalId((String) object.id);
                        switch (EntityType.fromType(resolvedGlobalId.type)) {
                            case EntityType.USER:
                                return OutputUserType
                            case EntityType.INVOICE:
                                return OutputInvoiceType
                            case EntityType.INVOICE_ITEM:
                                return OutputInvoiceItemType
                            case EntityType.CUSTOMER:
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

        Map<String, GraphQLFieldDefinition> commonInvoiceFieldDefinitions = [

                "number"      : GraphQLFieldDefinition.newFieldDefinition()
                                                      .name("number")
                                                      .type(Scalars.GraphQLLong)
                                                      .build(),

                "creationDate": GraphQLFieldDefinition.newFieldDefinition()
                                                      .name("creationDate")
                                                      .type(Scalars.GraphQLString)
                                                      .build(),

                "paymentDate" : GraphQLFieldDefinition.newFieldDefinition()
                                                      .name("paymentDate")
                                                      .type(Scalars.GraphQLString)
                                                      .build(),

                "paid"        : GraphQLFieldDefinition.newFieldDefinition()
                                                      .name("paid")
                                                      .type(Scalars.GraphQLBoolean)
                                                      .build(),

                "totalAmount" : GraphQLFieldDefinition.newFieldDefinition()
                                                      .name("totalAmount")
                                                      .type(Scalars.GraphQLBigDecimal)
                                                      .build()
        ]

        OutputInvoiceType = GraphQLObjectType.newObject()
                                             .name("Invoice")

                                             .field(commonInvoiceFieldDefinitions["number"])
                                             .field(commonInvoiceFieldDefinitions["creationDate"])
                                             .field(commonInvoiceFieldDefinitions["paymentDate"])
                                             .field(commonInvoiceFieldDefinitions["paid"])
                                             .field(commonInvoiceFieldDefinitions["totalAmount"])

                                             .field(GraphQLFieldDefinition.newFieldDefinition()
                                                                          .name("id")
                                                                          .type(new GraphQLNonNull(Scalars.GraphQLID))
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

        // Not recursion yet on self-referencing objects for inputs (see https://github.com/graphql-java/graphql-java/issues/172)
        // so GraphQLTypeReference can not be used as input objects yet
        GraphQLInputObjectType InputInvoiceType = GraphQLInputObjectType.newInputObject()
                                                                        .name("InputInvoice")

                                                                        .field(toInputField(commonInvoiceFieldDefinitions["number"]))
                                                                        .field(toInputField(commonInvoiceFieldDefinitions["creationDate"]))
                                                                        .field(toInputField(commonInvoiceFieldDefinitions["paymentDate"]))
                                                                        .field(toInputField(commonInvoiceFieldDefinitions["paid"]))
                                                                        .field(toInputField(commonInvoiceFieldDefinitions["totalAmount"]))

                                                                        .field(GraphQLInputObjectField.newInputObjectField()
                                                                                                      .name("id")
                                                                                                      .type(Scalars.GraphQLID) // Not Null is not Cool
                                                                                                      .build())

                                                                        .field(GraphQLInputObjectField.newInputObjectField()
                                                                                                      .name("user")
                                                                                                      .type(GraphQLInputObjectType.newInputObject()
                                                                                                                                  .name("InputInvoiceUserType")
                                                                                                                                  .field(GraphQLInputObjectField.newInputObjectField()
                                                                                                                                                                .name("id")
                                                                                                                                                                .type(new GraphQLNonNull(Scalars.GraphQLID))
                                                                                                                                                                .build())
                                                                                                                                  .build())
                                                                                                      .build())

                                                                        .field(GraphQLInputObjectField.newInputObjectField()
                                                                                                      .name("customer")
                                                                                                      .type(GraphQLInputObjectType.newInputObject()
                                                                                                                                  .name("InputInvoiceUserType")
                                                                                                                                  .field(GraphQLInputObjectField.newInputObjectField()
                                                                                                                                                                .name("id")
                                                                                                                                                                .type(new GraphQLNonNull(Scalars.GraphQLID))
                                                                                                                                                                .build())
                                                                                                                                  .build())
                                                                                                      .build())

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

        GraphQLFieldDefinition createInvoiceInputMutationDefinition = relay.mutationWithClientMutationId(
                "Invoice", "createInvoice",
                [GraphQLInputObjectField.newInputObjectField()
                                        .name("invoice")
                                        .type(InputInvoiceType).build()],
                [GraphQLFieldDefinition.newFieldDefinition()
                                       .name("invoice")
                                       .type(OutputInvoiceType).build()],
                createInvoiceMutationDataFetcher)

        GraphQLFieldDefinition updateInvoiceInputMutationDefinition = relay.mutationWithClientMutationId(
                "Invoice", "updateInvoice",
                [GraphQLInputObjectField.newInputObjectField()
                                        .name("invoice")
                                        .type(InputInvoiceType).build()],
                [GraphQLFieldDefinition.newFieldDefinition()
                                       .name("invoice")
                                       .type(OutputInvoiceType).build()],
                updateInvoiceMutationDataFetcher)

        GraphQLObjectType mutationType = GraphQLObjectType.newObject()
                                                          .name("invoicingMutation")
                                                          .field(updateInvoiceInputMutationDefinition)
                                                          .field(createInvoiceInputMutationDefinition)
                                                          .build();
        return GraphQLSchema.newSchema()
                            .query(queryType)
                            .mutation(mutationType)
                            .build();
    }

    private static GraphQLInputObjectField toInputField(GraphQLFieldDefinition sourceField) {
        return new GraphQLInputObjectField(sourceField.name, sourceField.description, (GraphQLInputType) sourceField.type, null)
    }

}
