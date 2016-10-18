package com.intuit.workshop.invoicing.graphql.repository.util

import com.intuit.workshop.invoicing.graphql.relay.GlobalIdHelper
import com.intuit.workshop.invoicing.graphql.schema.output.OutputCustomer
import com.intuit.workshop.invoicing.graphql.schema.output.OutputInvoice
import com.intuit.workshop.invoicing.graphql.schema.output.OutputInvoiceItem
import com.intuit.workshop.invoicing.graphql.schema.output.OutputUser

import java.text.SimpleDateFormat

// TODO Delete once Persistence is in place
class StaticModelBuilder {

    static final Date DATE = new SimpleDateFormat("yyyy/MM/dd").parse("2000/01/01")

    static OutputUser buildStaticModel() {
        OutputUser user = new OutputUser(id: id("/User", "user-1"), firstName: "First", lastName: "User", invoices: [])

        OutputCustomer firstCustomer = new OutputCustomer(id: id("/Customer", "customer-1"), businessName: "First Customer Ever", invoices: [])
        OutputInvoice firstInvoice = new OutputInvoice(id: id("/Invoice", "invoice-1"), user: user, number: 1, customer: firstCustomer,
                creationDate: DATE, paymentDate: DATE, paid: true, items: [], totalAmount: 300)
        firstInvoice.items << new OutputInvoiceItem(id: id("/InvoiceItem", "invoice-1-item-1"), name: "Bags", price: 100, invoice: firstInvoice)
        firstInvoice.items << new OutputInvoiceItem(id: id("/InvoiceItem", "invoice-1-item-2"), name: "Gloves", price: 200, invoice: firstInvoice)

        OutputCustomer secondCustomer = new OutputCustomer(id: id("/Customer","customer-2"), businessName: "Second Customer", invoices: [])
        OutputInvoice secondInvoice = new OutputInvoice(id: id("/Invoice", "invoice-2"), user: user, number: 2, customer: secondCustomer,
                creationDate: DATE, paid: false, items: [], totalAmount: 50)
        secondInvoice.items << new OutputInvoiceItem(id: id("/InvoiceItem", "invoice-2-item-1"), name: "Glasses", price: 50, invoice: secondInvoice)

        user.invoices = [firstInvoice, secondInvoice]
        firstCustomer.invoices << firstInvoice
        secondCustomer.invoices << secondInvoice

        return user
    }

    private static id(String type, String id) {
        return GlobalIdHelper.id(type, id)
    }

}
