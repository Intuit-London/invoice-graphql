package com.intuit.workshop.invoicing

import com.intuit.workshop.invoicing.domain.entity.id.EntityType
import com.intuit.workshop.invoicing.domain.entity.id.GlobalIdHelper
import com.intuit.workshop.invoicing.domain.repository.util.StaticModelBuilder
import com.intuit.workshop.invoicing.graphql.schema.InvoiceGraphQLSchemaFactory
import com.intuit.workshop.invoicing.graphql.schema.RelayMutation
import com.intuit.workshop.invoicing.util.SchemaSpecFixture
import graphql.ExecutionResult
import graphql.GraphQL
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLSchema
import spock.lang.Specification

class InvoiceSchemaSpec extends Specification {

    GraphQLSchema schema

    void setup() {
        schema = createSchema()
    }

    void "Query through the 'user' root field"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.USER_QUERY).getData();
        result == [
                users: [
                        [

                                id       : id("/User", "user-1"),
                                firstName: "First",
                                lastName : "User",
                                invoices : [
                                        [
                                                id          : id("/Invoice", "invoice-1"),
                                                user        : [
                                                        id: id("/User", "user-1")
                                                ],
                                                number      : 1,
                                                customer    : [
                                                        id          : id("/Customer", "customer-1"),
                                                        businessName: "First Customer Ever",
                                                        invoices    : [
                                                                [
                                                                        id: id("/Invoice", "invoice-1")
                                                                ]
                                                        ]

                                                ],
                                                creationDate: StaticModelBuilder.DATE.toString(),
                                                paymentDate : StaticModelBuilder.DATE.toString(),
                                                paid        : true,
                                                items       : [
                                                        [
                                                                id     : id("/InvoiceItem", "invoice-1-item-1"),
                                                                invoice: [
                                                                        id: id("/Invoice", "invoice-1")
                                                                ],
                                                                name   : "Bags",
                                                                price  : 100
                                                        ],
                                                        [
                                                                id     : id("/InvoiceItem", "invoice-1-item-2"),
                                                                invoice: [
                                                                        id: id("/Invoice", "invoice-1")
                                                                ],
                                                                name   : "Gloves",
                                                                price  : 200
                                                        ]
                                                ],
                                                totalAmount : 300

                                        ],
                                        [

                                                id          : id("/Invoice", "invoice-2"),
                                                user        : [
                                                        id: id("/User", "user-1")
                                                ],
                                                number      : 2,
                                                customer    : [
                                                        id          : id("/Customer", "customer-2"),
                                                        businessName: "Second Customer",
                                                        invoices    : [
                                                                [
                                                                        id: id("/Invoice", "invoice-2")
                                                                ]
                                                        ]

                                                ],
                                                creationDate: StaticModelBuilder.DATE.toString(),
                                                paymentDate : null,
                                                paid        : false,
                                                items       : [
                                                        [
                                                                id     : id("/InvoiceItem", "invoice-2-item-1"),
                                                                invoice: [
                                                                        id: id("/Invoice", "invoice-2")
                                                                ],
                                                                name   : "Glasses",
                                                                price  : 50
                                                        ],
                                                ],
                                                totalAmount : 50
                                        ]
                                ]
                        ]
                ]
        ]
    }

    void "Filter through the 'user' root query"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.USER_QUERY_FILTERED).getData();
        result == [
                users: [
                        [

                                id       : id("/User", "user-1"),
                                firstName: "First",
                                lastName : "User",
                        ]

                ]
        ]
    }

    void "Query through the 'node' root field"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.NODE_QUERY).getData();
        result == [
                node: [
                        id: id("/User", "user-1")
                ]
        ]
    }

    void "Relay-Compliant create mutation of a full invoice as an input"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.RELAY_CREATE_INVOICE_MUTATION).getData();
        result == [
                createInvoice: [
                        clientMutationId: "client-mutation-1",
                        invoice         :
                                [
                                        id: id("/Invoice", "invoice-1")
                                ]

                ]
        ]
    }

    void "Relay-Compliant update mutation of a full invoice as an input"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.RELAY_INPUT_UPDATE_MUTATION).getData();
        result == [
                updateInvoice: [
                        clientMutationId: "client-mutation-1",
                        invoice         :
                                [
                                        id: id("/Invoice", "invoice-1")
                                ]

                ]
        ]
    }

    void "Relay-Compliant mutation of a full invoice as an output"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.RELAY_OUTPUT_UPDATE_MUTATION).getData();
        result == [
                updateInvoice: [
                        clientMutationId: "client-mutation-1",
                        invoice         :
                                [
                                        id          : id("/Invoice", "invoice-1"),
                                        user        : [
                                                id: id("/User", "user-1")
                                        ],
                                        number      : 1,
                                        customer    : [
                                                id          : id("/Customer", "customer-1"),
                                                businessName: "First Customer Ever",
                                                invoices    : [
                                                        [
                                                                id: id("/Invoice", "invoice-1")
                                                        ]
                                                ]

                                        ],
                                        creationDate: StaticModelBuilder.DATE.toString(),
                                        paymentDate : StaticModelBuilder.DATE.toString(),
                                        paid        : true,
                                        items       : [
                                                [
                                                        id     : id("/InvoiceItem", "invoice-1-item-1"),
                                                        invoice: [
                                                                id: id("/Invoice", "invoice-1")
                                                        ],
                                                        name   : "Bags",
                                                        price  : 100
                                                ],
                                                [
                                                        id     : id("/InvoiceItem", "invoice-1-item-2"),
                                                        invoice: [
                                                                id: id("/Invoice", "invoice-1")
                                                        ],
                                                        name   : "Gloves",
                                                        price  : 200
                                                ]
                                        ],
                                        totalAmount : 300

                                ],

                ]
        ]
    }

    void "Relay-Compliant create mutation of a full invoice item as an input"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.RELAY_CREATE_INVOICE_ITEM_MUTATION).getData();
        result == [
                createInvoiceItem: [
                        clientMutationId: "client-mutation-1",
                        invoiceItem     :
                                [
                                        id: id("/InvoiceItem", "invoice-1-item-1")
                                ]

                ]
        ]
    }

    void "Relay-Compliant update mutation of a full invoice item as an input"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.RELAY_UPDATE_INVOICE_ITEM_MUTATION).getData();
        result == [
                updateInvoiceItem: [
                        clientMutationId: "client-mutation-1",
                        invoiceItem     :
                                [
                                        id: id("/InvoiceItem", "invoice-1-item-1")
                                ]

                ]
        ]
    }

    private ExecutionResult successExecution(String query) {
        ExecutionResult executionResult = new GraphQL(schema).execute(query)
        assert executionResult.getErrors() == [], "No errors are expected, but found: ${executionResult.getErrors()}"
        return executionResult
    }

    private createSchema() {
        InvoiceGraphQLSchemaFactory graphQLSchemaFactory = new InvoiceGraphQLSchemaFactory()
        graphQLSchemaFactory.userQueryDataFetcher = new MockRootQueryDataFetcher()
        graphQLSchemaFactory.nodeQueryDataFetcher = new MockNodeQueryDataFetcher()
        graphQLSchemaFactory.createInvoiceMutationDataFetcher = new MockMutationDataFetcher(EntityType.INVOICE)
        graphQLSchemaFactory.updateInvoiceMutationDataFetcher = new MockMutationDataFetcher(EntityType.INVOICE)
        graphQLSchemaFactory.createInvoiceItemMutationDataFetcher = new MockMutationDataFetcher(EntityType.INVOICE_ITEM)
        graphQLSchemaFactory.updateInvoiceItemMutationDataFetcher = new MockMutationDataFetcher(EntityType.INVOICE_ITEM)
        return graphQLSchemaFactory.build()
    }

    private String id(String type, String id) {
        return GlobalIdHelper.id(type, id)
    }

    class MockRootQueryDataFetcher implements DataFetcher {

        @Override
        Object get(DataFetchingEnvironment environment) {
            String globalId = (String) environment.arguments.id
            if (globalId) {
                return [SchemaSpecFixture.build().users().find { it.id == globalId }]
            }
            return SchemaSpecFixture.build().users()
        }
    }

    class MockNodeQueryDataFetcher implements DataFetcher {

        @Override
        Object get(DataFetchingEnvironment environment) {
            return SchemaSpecFixture.build().users().first()
        }

    }

    class MockMutationDataFetcher implements DataFetcher {

        EntityType type

        MockMutationDataFetcher(EntityType type) {
            this.type = type
        }

        @Override
        Object get(DataFetchingEnvironment environment) {
            if (type == EntityType.INVOICE) {
                return new RelayMutation(clientMutationId: "client-mutation-1", invoice: SchemaSpecFixture.build().firstInvoice())
            }
            if (type == EntityType.INVOICE_ITEM) {
                return new RelayMutation(clientMutationId: "client-mutation-1", invoiceItem: SchemaSpecFixture.build().firstInvoiceItem())
            }
        }
    }

}


