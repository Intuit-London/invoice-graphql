package com.intuit.workshop.invoicing.domain.repository.util

import com.intuit.workshop.invoicing.domain.entity.id.GlobalIdHelper
import com.intuit.workshop.invoicing.domain.entity.Customer
import com.intuit.workshop.invoicing.domain.entity.Invoice
import com.intuit.workshop.invoicing.domain.entity.InvoiceItem
import com.intuit.workshop.invoicing.domain.entity.User

import java.text.SimpleDateFormat

// TODO Delete once Persistence is in place
class StaticModelBuilder {

    static final Date DATE = new SimpleDateFormat("yyyy/MM/dd").parse("1927/10/01")

    static List<User> buildStaticModel() {
        User user = new User(id: id("/User", "user-1"), firstName: "Alexander", lastName: "Fleming", invoices: [])

        Customer firstCustomer = new Customer(id: id("/Customer", "customer-1"), businessName: "Erns Boris Chain", invoices: [])
        Invoice firstInvoice = new Invoice(id: id("/Invoice", "invoice-1"), user: user, number: 1, customer: firstCustomer,
                creationDate: DATE, paymentDate: DATE, paid: true, items: [], totalAmount: 300)
        firstInvoice.items << new InvoiceItem(id: id("/InvoiceItem", "invoice-1-item-1"), name: "Petri Dishes", price: 100, invoice: firstInvoice)
        firstInvoice.items << new InvoiceItem(id: id("/InvoiceItem", "invoice-1-item-2"), name: "Gloves", price: 200, invoice: firstInvoice)

        Customer secondCustomer = new Customer(id: id("/Customer","customer-2"), businessName: "Howard Florey", invoices: [])
        Invoice secondInvoice = new Invoice(id: id("/Invoice", "invoice-2"), user: user, number: 2, customer: secondCustomer,
                creationDate: DATE, paid: false, items: [], totalAmount: 50)
        secondInvoice.items << new InvoiceItem(id: id("/InvoiceItem", "invoice-2-item-1"), name: "Staphylococcus plate culture", price: 50, invoice: secondInvoice)

        user.invoices = [firstInvoice, secondInvoice]
        firstCustomer.invoices << firstInvoice
        secondCustomer.invoices << secondInvoice

        return [user]
    }

    private static id(String type, String id) {
        return GlobalIdHelper.id(type, id)
    }

}
