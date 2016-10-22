package com.intuit.workshop.invoicing.domain.repository

import com.intuit.workshop.invoicing.domain.entity.Customer
import com.intuit.workshop.invoicing.domain.entity.InvoiceItem
import com.intuit.workshop.invoicing.domain.repository.util.StaticModelBuilder
import com.intuit.workshop.invoicing.domain.entity.Invoice
import com.intuit.workshop.invoicing.domain.entity.User
import com.intuit.workshop.invoicing.domain.entity.id.GlobalIdHelper
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository
import org.springframework.util.Assert

import javax.annotation.PostConstruct

@Slf4j
@Repository
class InvoiceRepository {

    private List<User> users

    @PostConstruct
    void init() {
        users = StaticModelBuilder.buildStaticModel()
    }

    /* QUERIES */

    List<User> findUsers() {
        return users
    }

    User getUserById(String id) {
        return (User) users.find { it.id == id }
    }

    Customer getCustomerById(String id) {
        return (Customer) users.invoices.flatten().collect { it.customer }.find { it.id == id }
    }

    Invoice getInvoiceById(String id) {
        return (Invoice) users.invoices.flatten().find { it.id == id }
    }

    InvoiceItem getInvoiceItemById(String id) {
        return (InvoiceItem) users.invoices.flatten().collect { it.items }.flatten().find { it.id == id }
    }

    /* COMMANDS */

    Invoice saveInvoice(Invoice invoice) {
        User user = getUserById(invoice.user?.id)
        Assert.notNull(user, "Can't save invoice: parent not found")
        id(invoice)
        invoice.user = user
        user.invoices.removeAll { it.id.equals(invoice.id) }
        user.invoices.add(invoice)
        return invoice
    }

    InvoiceItem saveInvoiceItem(InvoiceItem invoiceItem) {
        Invoice invoice = getInvoiceById(invoiceItem.invoice?.id)
        Assert.notNull(invoice, "Can't save invoice item: parent not found")
        id(invoiceItem)
        invoiceItem.invoice = invoice
        invoice.items.removeAll { it.id.equals(invoiceItem.id) }
        invoice.items.add(invoiceItem)
        return invoiceItem
    }

    private static void id(entity)  {
        GlobalIdHelper.validate(entity.id)
        entity.id = entity.id ?: GlobalIdHelper.entityId(entity, UUID.randomUUID().toString())
    }

}
