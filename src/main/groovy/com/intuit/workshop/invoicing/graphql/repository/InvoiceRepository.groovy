package com.intuit.workshop.invoicing.graphql.repository

import com.intuit.workshop.invoicing.graphql.repository.util.StaticModelBuilder
import com.intuit.workshop.invoicing.graphql.schema.output.Invoice
import com.intuit.workshop.invoicing.graphql.schema.output.User
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository

import javax.annotation.PostConstruct

@Slf4j
@Repository
class InvoiceRepository {

    private List<User> users

    @PostConstruct
    void init() {
        users = StaticModelBuilder.buildStaticModel()
    }

    List<User> findUsers() {
        return users
    }

    User getUserById(String id) {
        return users.find { it.id == id }
    }

    Invoice getInvoiceById(String id) {
        return users.invoices.flatten().find { it.id == id }
    }

}
