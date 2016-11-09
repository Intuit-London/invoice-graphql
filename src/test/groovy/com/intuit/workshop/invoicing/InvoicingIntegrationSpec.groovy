package com.intuit.workshop.invoicing

import com.intuit.workshop.Application
import com.intuit.workshop.invoicing.domain.entity.id.GlobalIdHelper
import com.intuit.workshop.invoicing.domain.repository.util.StaticModelBuilder
import com.intuit.workshop.invoicing.graphql.service.GraphQLExecutionService
import com.intuit.workshop.invoicing.util.SchemaSpecFixture
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest
@ContextConfiguration(classes = Application)
class InvoicingIntegrationSpec extends Specification {

    @Autowired
    GraphQLExecutionService executionService

    void "Query through the 'user' root field"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.USER_QUERY);
        result == [
                users: [
                        [

                                id       : id("/User", "user-1"),
                                firstName: "Alexander",
                                lastName : "Fleming",
                                invoices : [
                                        [
                                                id          : id("/Invoice", "invoice-1"),
                                                user        : [
                                                        id: id("/User", "user-1")
                                                ],
                                                number      : 1,
                                                customer    : [
                                                        id          : id("/Customer", "customer-1"),
                                                        businessName: "Erns Boris Chain",
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
                                                                name   : "Petri Dishes",
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
                                                        businessName: "Howard Florey",
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
                                                                name   : "Staphylococcus plate culture",
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

    void "Filter through the 'user' root query using a single id"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.USER_QUERY_FILTERED_SINGLE_ID)
        result == [
                users: [
                        [

                                id       : id("/User", "user-1"),
                                firstName: "Alexander",
                                lastName : "Fleming",
                        ]

                ]
        ]
    }

    void "Filter through the 'user' root query using a list of ids"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.USER_QUERY_FILTERED_LIST_IDS)
        result == [
                users: [
                        [

                                id       : id("/User", "user-1"),
                                firstName: "Alexander",
                                lastName : "Fleming",
                        ]

                ]
        ]
    }

    void "Query through the 'node' root field"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.NODE_QUERY)
        result == [
                node: [
                        id: id("/User", "user-1")
                ]
        ]
    }

    void "Relay-Compliant create mutation of a full invoice as an input"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.RELAY_CREATE_INVOICE_MUTATION)
        result == [
                createInvoice: [
                        clientMutationId: "client-mutation-1",
                        invoice         :
                                [
                                        user       :
                                                [
                                                        id: id("/User", "user-1")
                                                ],
                                        number     : 1234,
                                        totalAmount: 100
                                ]

                ]
        ]
    }

    void "Relay-Compliant update mutation of a full invoice as an input"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.RELAY_INPUT_UPDATE_MUTATION)
        result == [
                updateInvoice: [
                        clientMutationId: "client-mutation-1",
                        invoice         :
                                [
                                        user       :
                                                [
                                                        id: id("/User", "user-1")
                                                ],
                                        number     : 1234,
                                        totalAmount: 100
                                ]
                ]
        ]
    }

    void "Relay-Compliant create mutation of a full invoice item as an input"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.RELAY_CREATE_INVOICE_ITEM_MUTATION)
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
        Map<String, Object> result = successExecution(SchemaSpecFixture.RELAY_UPDATE_INVOICE_ITEM_MUTATION)
        result == [
                updateInvoiceItem: [
                        clientMutationId: "client-mutation-1",
                        invoiceItem     :
                                [
                                        id: id("/InvoiceItem", "invoice-1-item-1"),
                                        name: "Food",
                                        price: 1000
                                ]

                ]
        ]
    }

    void "Relay-Compliant create mutation using an input variable"() {
        expect:
        Map<String, Object> result = successExecution(
                SchemaSpecFixture.RELAY_CREATE_INVOICE_MUTATION_WITH_VARIABLE,
                SchemaSpecFixture.RELAY_CREATE_INVOICE_VARIABLE)
        result == [
                createInvoice: [
                        clientMutationId: "client-mutation-1",
                        invoice         :
                                [
                                        user       :
                                                [
                                                        id: id("/User", "user-1")
                                                ],
                                        number     : 35,
                                        totalAmount: 234.34
                                ]

                ]
        ]
    }

    private Map<String, Object> successExecution(String query, String variables = "") {
        Map<String, Object> result = executionService.execute(query, parseVariables(variables))
        assert !result.errors, "No errors are expected, but found: ${result.errors}"
        return (Map) result.data
    }

    private String id(String type, String id) {
        return GlobalIdHelper.id(type, id)
    }

    private static Map<String, Object> parseVariables(String variables) {
        if (variables && variables != "") {
            return (Map<String, Object>) new JsonSlurper().parseText(variables)
        }
        return null
    }
}
