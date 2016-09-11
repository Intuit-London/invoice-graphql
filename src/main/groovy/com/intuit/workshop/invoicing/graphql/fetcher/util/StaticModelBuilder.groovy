package com.intuit.workshop.invoicing.graphql.fetcher.util

import com.intuit.workshop.invoicing.model.Customer
import com.intuit.workshop.invoicing.model.Invoice
import com.intuit.workshop.invoicing.model.InvoiceItem
import com.intuit.workshop.invoicing.model.User

import java.text.SimpleDateFormat

// TODO Delete once Persistence is in place
class StaticModelBuilder {

    static final Date DATE = new SimpleDateFormat("yyyy/MM/dd").parse("2000/01/01")

    static User buildStaticModel() {
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

        return user
    }

}
