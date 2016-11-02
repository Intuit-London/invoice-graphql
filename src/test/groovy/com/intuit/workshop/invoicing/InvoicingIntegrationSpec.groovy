package com.intuit.workshop.invoicing

import com.intuit.workshop.Application
import com.intuit.workshop.invoicing.domain.entity.id.GlobalIdHelper
import com.intuit.workshop.invoicing.domain.repository.util.StaticModelBuilder
import com.intuit.workshop.invoicing.graphql.service.GraphQLExecutionService
import com.intuit.workshop.invoicing.util.SchemaSpecFixture
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

    void "Filter through the 'user' root query using a single id"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.USER_QUERY_FILTERED_SINGLE_ID)
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

    void "Filter through the 'user' root query using a list of ids"() {
        expect:
        Map<String, Object> result = successExecution(SchemaSpecFixture.USER_QUERY_FILTERED_LIST_IDS)
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

    private Map<String, Object> successExecution(String query) {
        Map<String, Object> result = executionService.execute(query)
        assert !result.errors, "No errors are expected, but found: ${result.errors}"
        return (Map) result.data
    }

    private String id(String type, String id) {
        return GlobalIdHelper.id(type, id)
    }
}
