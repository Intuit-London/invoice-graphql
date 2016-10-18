package com.intuit.workshop.invoicing

import com.intuit.workshop.invoicing.graphql.relay.GlobalIdHelper
import com.intuit.workshop.invoicing.graphql.repository.util.StaticModelBuilder
import com.intuit.workshop.invoicing.graphql.schema.InvoiceGraphQLSchemaFactory
import com.intuit.workshop.invoicing.graphql.schema.output.OutputRelayMutation
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
                user: [
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
                        ],
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

    void "Relay-Compliant mutation of a full invoice as an input"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.RELAY_INPUT_MUTATION).getData();
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
        Map<String, Object> result = successExecution(SchemaSpecFixture.RELAY_OUTPUT_MUTATION).getData();
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

    private ExecutionResult successExecution(String query) {
        ExecutionResult executionResult = new GraphQL(schema).execute(query)
        assert executionResult.getErrors() == [], "No errors are expected, but found: ${executionResult.getErrors()}"
        return executionResult
    }

    private createSchema() {
        InvoiceGraphQLSchemaFactory graphQLSchemaFactory = new InvoiceGraphQLSchemaFactory()
        graphQLSchemaFactory.userQueryDataFetcher = new MockQueryDataFetcher()
        graphQLSchemaFactory.nodeQueryDataFetcher = new MockQueryDataFetcher()
        graphQLSchemaFactory.invoiceMutationDataFetcher = new MockMutationDataFetcher()
        return graphQLSchemaFactory.build()
    }

    private String id(String type, String id) {
        return GlobalIdHelper.id(type, id)
    }

    class MockQueryDataFetcher implements DataFetcher {

        @Override
        Object get(DataFetchingEnvironment environment) {
            return SchemaSpecFixture.build().user()
        }
    }

    class MockMutationDataFetcher implements DataFetcher {

        @Override
        Object get(DataFetchingEnvironment environment) {
            return new OutputRelayMutation(clientMutationId: "client-mutation-1", invoice: SchemaSpecFixture.build().firstInvoice())
        }
    }

}


