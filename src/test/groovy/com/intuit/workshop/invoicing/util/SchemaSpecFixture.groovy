package com.intuit.workshop.invoicing.util

import com.intuit.workshop.invoicing.graphql.fetcher.util.StaticModelBuilder
import com.intuit.workshop.invoicing.model.Invoice
import com.intuit.workshop.invoicing.model.User

class SchemaSpecFixture {

    static final String QUERY =
            """
{
    user {
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

    static final String RELAY_INPUT_MUTATION =
            """
mutation InvoiceMutation {
    updateInvoice(input: {
        clientMutationId: "client-mutation-1",
        invoice: {
            user: {
                id: "user-1"
            },
            number: 1234,
            customer: {
                id: "customer-2"
            },
            creationDate: "Sat Jan 01 00:00:00 GMT 2000",
            paymentDate: "Sat Jan 01 00:00:00 GMT 2000",
            paid: true,
            items: [],
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

    static final String RELAY_OUTPUT_MUTATION =
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

    private User user

    static SchemaSpecFixture build() {
        return new SchemaSpecFixture(user: StaticModelBuilder.buildStaticModel())
    }

    User user() {
        return user
    }

    Invoice firstInvoice() {
        user.invoices.find { it.id == "invoice-1" }
    }

}

