package com.intuit.workshop.invoicing.util

import com.intuit.workshop.invoicing.domain.entity.id.GlobalIdHelper
import com.intuit.workshop.invoicing.domain.repository.util.StaticModelBuilder
import com.intuit.workshop.invoicing.domain.entity.Invoice
import com.intuit.workshop.invoicing.domain.entity.User

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

    static final String NODE_QUERY =
            """
{
    node(id: "ID") {
        id
    }
}
"""

    static final String RELAY_CREATE_MUTATION =
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
                id: "customer-2"
            },
            creationDate: "Sat Jan 01 00:00:00 GMT 2000",
            paymentDate: "Sat Jan 01 00:00:00 GMT 2000",
            paid: true,
            totalAmount: 100
        }
    }) {
        clientMutationId
        invoice {
            id
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
            user: {
                id: "${GlobalIdHelper.id("/User", "user-1")}"
            },
            number: 1234,
            customer: {
                id: "customer-2"
            },
            creationDate: "Sat Jan 01 00:00:00 GMT 2000",
            paymentDate: "Sat Jan 01 00:00:00 GMT 2000",
            paid: true,
            totalAmount: 100
        }
    }) {
        clientMutationId
        invoice {
            id
        }
    }
}
"""

    static final String RELAY_OUTPUT_UPDATE_MUTATION =
            """
mutation InvoiceMutation {
    updateInvoice(input: {
        clientMutationId: "client-mutation-1",
    }) {
        clientMutationId
        invoice {
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

    private List<User> users

    List<User> users() {
        return users
    }

    Invoice firstInvoice() {
        return (Invoice) users.collect { it.invoices }.flatten().find { it.id == GlobalIdHelper.id("/Invoice", "invoice-1") }
    }

    static SchemaSpecFixture build() {
        return new SchemaSpecFixture(users: StaticModelBuilder.buildStaticModel())
    }

}

