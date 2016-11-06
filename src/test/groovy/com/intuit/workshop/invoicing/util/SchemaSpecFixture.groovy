package com.intuit.workshop.invoicing.util

import com.intuit.workshop.invoicing.domain.entity.id.GlobalIdHelper

class SchemaSpecFixture {

    static final String USER_QUERY =
            """
{
    users {
        id
        firstName
        lastName
        invoices {
            id
            user {
                id
            }
            number
            customer {
                id
                businessName
                invoices {
                    id
                }
            }
            creationDate
            paymentDate
            paid
            items {
                id
                invoice {
                    id
                }
                name
                price
            }
            totalAmount
        }
    }
}
"""

    static final String USER_QUERY_FILTERED_SINGLE_ID =
            """
{
    users(id: "${GlobalIdHelper.id("/User", "user-1")}") {
        id
        firstName
        lastName
    }
}
"""

    static final String USER_QUERY_FILTERED_LIST_IDS =
            """
{
    users(id: [ "${GlobalIdHelper.id("/User", "user-1")}", "${GlobalIdHelper.id("/User", "user-2")}" ]) {
        id
        firstName
        lastName
    }
}
"""

    static final String NODE_QUERY =
            """
{
    node(id: "${GlobalIdHelper.id("/User", "user-1")}") {
        id
    }
}
"""

    static final String RELAY_CREATE_INVOICE_MUTATION =
            """
mutation InvoiceMutation {
    createInvoice(input: {
        clientMutationId: "client-mutation-1",
        invoice: {
            user: {
                id: "${GlobalIdHelper.id("/User", "user-1")}"
            },
            number: 1234,
            customer: {
                id: "customer-2",
                businessName: "Juan"
            },
            creationDate: "Sat Jan 01 00:00:00 GMT 2000",
            paymentDate: "Sat Jan 01 00:00:00 GMT 2000",
            paid: true,
            totalAmount: 100
        }
    }) {
        clientMutationId
        invoice {
            user {
                id
            }
            number
            totalAmount
        }
    }
}
"""

    static final String RELAY_INPUT_UPDATE_MUTATION =
            """
mutation InvoiceMutation {
    updateInvoice(input: {
        clientMutationId: "client-mutation-1",
        invoice: {
            id: "${GlobalIdHelper.id("/Invoice", "invoice-1")}",
            user: {
                id: "${GlobalIdHelper.id("/User", "user-1")}"
            },
            number: 1234,
            customer: {
                id: "customer-2",
                businessName: "Juan"
            },
            creationDate: "Sat Jan 01 00:00:00 GMT 2000",
            paymentDate: "Sat Jan 01 00:00:00 GMT 2000",
            paid: true,
            totalAmount: 100
        }
    }) {
        clientMutationId
        invoice {
            user {
                id
            }
            number
            totalAmount
        }
    }
}
"""

    static final String RELAY_CREATE_INVOICE_ITEM_MUTATION =
            """
mutation InvoiceMutation {
    createInvoiceItem(input: {
        clientMutationId: "client-mutation-1",
        invoiceItem: {
            id: "${GlobalIdHelper.id("/InvoiceItem", "invoice-1-item-1")}"
            invoice: {
                id: "${GlobalIdHelper.id("/Invoice", "invoice-1")}"
            },
            name: "Food",
            price: 100
        }
    }) {
        clientMutationId
        invoiceItem {
            id
        }
    }
}
"""

    static final String RELAY_UPDATE_INVOICE_ITEM_MUTATION =
            """
mutation InvoiceMutation {
    updateInvoiceItem(input: {
        clientMutationId: "client-mutation-1",
        invoiceItem: {
            id: "${GlobalIdHelper.id("/InvoiceItem", "invoice-1-item-1")}"
            invoice: {
                id: "${GlobalIdHelper.id("/Invoice", "invoice-1")}"
            },
            name: "Food",
            price: 1000
        }
    }) {
        clientMutationId
        invoiceItem {
            id
            name
            price
        }
    }
}
"""

    static final String RELAY_CREATE_INVOICE_MUTATION_WITH_VARIABLE =
            """
mutation CreateInvoice(\$input_0:CreateInvoiceInput!) {
    createInvoice(input:\$input_0) {
        clientMutationId
        invoice {
            user {
                id
            }
            number
            totalAmount
        }
    }
}
"""

    static final String RELAY_CREATE_INVOICE_VARIABLE =
            """
{
    "input_0": {
        "invoice": {
            "number":"35",
            "creationDate": "25-06-2016",
            "paid": false,
            "customer": {
                "id": "${GlobalIdHelper.id("/Customer", "customer-1")}",
                "businessName":"asfa"
            },
            "user" : {
                "id": "${GlobalIdHelper.id("/User", "user-1")}"
            },
            "totalAmount": 234.34
        },
        "clientMutationId": "client-mutation-1"
    }
}
"""

}
