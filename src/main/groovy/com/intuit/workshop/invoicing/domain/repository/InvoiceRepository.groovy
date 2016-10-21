package com.intuit.workshop.invoicing.domain.repository

import com.intuit.workshop.invoicing.domain.entity.Customer
import com.intuit.workshop.invoicing.domain.entity.Entity
import com.intuit.workshop.invoicing.domain.entity.InvoiceItem
import com.intuit.workshop.invoicing.domain.repository.util.StaticModelBuilder
import com.intuit.workshop.invoicing.domain.entity.Invoice
import com.intuit.workshop.invoicing.domain.entity.User
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
        user.invoices.remove { it.id == invoice.id }
        user.invoices.add(invoice)
        return invoice
    }

    private static void id(entity)  {
        entity.id = entity.id ?: UUID.randomUUID()
    }

}
