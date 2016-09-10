package com.intuit.workshop.invoicing

import com.intuit.workshop.invoicing.model.Customer
import com.intuit.workshop.invoicing.model.Invoice
import com.intuit.workshop.invoicing.model.InvoiceItem
import com.intuit.workshop.invoicing.model.User

import java.text.SimpleDateFormat

class SchemaSpecFixture {

    static final Date DATE = new SimpleDateFormat("yyyy/MM/dd").parse("2000/01/01")

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
        User user = new User(id: "user-1", firstName: "First", lastName: "User", invoices: [])

        Customer firstCustomer = new Customer(id: "customer-1", businessName: "First Customer Ever", invoices: [])
        Invoice firstInvoice = new Invoice(id: "invoice-1", user: user, number: 1, customer: firstCustomer,
                creationDate: DATE, paymentDate: DATE, paid: true, items: [], totalAmount: 300)
        firstInvoice.items << new InvoiceItem(id: "invoice-1-item-1", name: "Bags", price: 100, invoice: firstInvoice)
        firstInvoice.items << new InvoiceItem(id: "invoice-1-item-2", name: "Gloves", price: 200, invoice: firstInvoice)

        Customer secondCustomer = new Customer(id: "customer-2", businessName: "Second Customer", invoices: [])
        Invoice secondInvoice = new Invoice(id: "invoice-2", user: user, number: 2, customer: secondCustomer,
                creationDate: DATE, paid: false, items: [], totalAmount: 50)
        secondInvoice.items << new InvoiceItem(id: "invoice-2-item-1", name: "Glasses", price: 50, invoice: secondInvoice)

        user.invoices = [firstInvoice, secondInvoice]
        firstCustomer.invoices << firstInvoice
        secondCustomer.invoices << secondInvoice

        return new SchemaSpecFixture(user: user)
    }

    User user() {
        return user
    }

    Invoice firstInvoice() {
        user.invoices.find { it.id == "invoice-1" }
    }

}

