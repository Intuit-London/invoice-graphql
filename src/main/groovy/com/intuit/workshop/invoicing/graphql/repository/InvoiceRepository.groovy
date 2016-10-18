package com.intuit.workshop.invoicing.graphql.repository

import com.intuit.workshop.invoicing.graphql.repository.util.StaticModelBuilder
import com.intuit.workshop.invoicing.graphql.schema.output.OutputInvoice
import com.intuit.workshop.invoicing.graphql.schema.output.OutputUser
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository

import javax.annotation.PostConstruct

@Slf4j
@Repository
class InvoiceRepository {

    private List<OutputUser> users

    @PostConstruct
    void init() {
        users = [StaticModelBuilder.buildStaticModel()]
    }

    OutputUser findUsers() {
        return users
    }

    OutputUser getUserById(String id) {
        return users.find { it.id == id }
    }

    OutputInvoice getInvoiceById(String id) {
        return users.invoices.flatten().find { it.id == id }
    }

}
