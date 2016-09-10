package com.intuit.workshop.invoicing

import com.intuit.workshop.invoicing.graphql.GraphQLSchemaHolder
import com.intuit.workshop.invoicing.model.Invoice
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

    void "Query the full schema"() {
        expect:
        Map<String, Object> result = new GraphQL(schema).execute(SchemaSpecFixture.QUERY).getData();
        result == [
                user: [
                        id       : "user-1",
                        firstName: "First",
                        lastName : "User",
                        invoices : [
                                [
                                        id          : "invoice-1",
                                        user        : [
                                                id: "user-1"
                                        ],
                                        number      : 1,
                                        customer    : [
                                                id          : "customer-1",
                                                businessName: "First Customer Ever",
                                                invoices    : [
                                                        [
                                                                id: "invoice-1"
                                                        ]
                                                ]

                                        ],
                                        creationDate: SchemaSpecFixture.DATE.toString(),
                                        paymentDate : SchemaSpecFixture.DATE.toString(),
                                        paid        : true,
                                        items       : [
                                                [
                                                        id     : "invoice-1-item-1",
                                                        invoice: [
                                                                id: "invoice-1"
                                                        ],
                                                        name   : "Bags",
                                                        price  : 100
                                                ],
                                                [
                                                        id     : "invoice-1-item-2",
                                                        invoice: [
                                                                id: "invoice-1"
                                                        ],
                                                        name   : "Gloves",
                                                        price  : 200
                                                ]
                                        ],
                                        totalAmount : 300

                                ],
                                [

                                        id          : "invoice-2",
                                        user        : [
                                                id: "user-1"
                                        ],
                                        number      : 2,
                                        customer    : [
                                                id          : "customer-2",
                                                businessName: "Second Customer",
                                                invoices    : [
                                                        [
                                                                id: "invoice-2"
                                                        ]
                                                ]

                                        ],
                                        creationDate: SchemaSpecFixture.DATE.toString(),
                                        paymentDate : null,
                                        paid        : false,
                                        items       : [
                                                [
                                                        id     : "invoice-2-item-1",
                                                        invoice: [
                                                                id: "invoice-2"
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

    void "Relay-Compliant mutation of a full invoice as an input"() {
        expect:
        Map<String, Object> result = new GraphQL(schema).execute(SchemaSpecFixture.RELAY_INPUT_MUTATION).getData();
        result == [
                updateInvoice: [
                        clientMutationId: "client-mutation-1",
                        invoice         :
                                [
                                        id: "invoice-1"
                                ]

                ]
        ]
    }

    void "Relay-Compliant mutation of a full invoice as an output"() {
        expect:
        Map<String, Object> result = new GraphQL(schema).execute(SchemaSpecFixture.RELAY_OUTPUT_MUTATION).getData();
        result == [
                updateInvoice: [
                        clientMutationId: "client-mutation-1",
                        invoice         :
                                [
                                        id          : "invoice-1",
                                        user        : [
                                                id: "user-1"
                                        ],
                                        number      : 1,
                                        customer    : [
                                                id          : "customer-1",
                                                businessName: "First Customer Ever",
                                                invoices    : [
                                                        [
                                                                id: "invoice-1"
                                                        ]
                                                ]

                                        ],
                                        creationDate: SchemaSpecFixture.DATE.toString(),
                                        paymentDate : SchemaSpecFixture.DATE.toString(),
                                        paid        : true,
                                        items       : [
                                                [
                                                        id     : "invoice-1-item-1",
                                                        invoice: [
                                                                id: "invoice-1"
                                                        ],
                                                        name   : "Bags",
                                                        price  : 100
                                                ],
                                                [
                                                        id     : "invoice-1-item-2",
                                                        invoice: [
                                                                id: "invoice-1"
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

    private createSchema() {
        GraphQLSchemaHolder schemaHolder = new GraphQLSchemaHolder()
        schemaHolder.rootQueryDataFetcher = new MockRootQueryDataFetcher()
        schemaHolder.invoiceMutationDataFetcher = new MockMutationDataFetcher()
        schemaHolder.buildGraphQLSchema()
        return schemaHolder.graphQLSchema
    }

    class MockRootQueryDataFetcher implements DataFetcher {

        @Override
        Object get(DataFetchingEnvironment environment) {
            return SchemaSpecFixture.build().user()
        }
    }

    class MockMutationDataFetcher implements DataFetcher {

        @Override
        Object get(DataFetchingEnvironment environment) {
            return new RelayMutationResponse(clientMutationId: "client-mutation-1", invoice: SchemaSpecFixture.build().firstInvoice())
        }

        class RelayMutationResponse {

            String clientMutationId

            Invoice invoice
        }
    }

}


