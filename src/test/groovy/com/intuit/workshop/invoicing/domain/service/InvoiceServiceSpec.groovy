package com.intuit.workshop.invoicing.domain.service

import com.intuit.workshop.invoicing.domain.entity.Invoice
import com.intuit.workshop.invoicing.domain.entity.User
import com.intuit.workshop.invoicing.domain.repository.InvoiceRepository
import spock.lang.Specification

class InvoiceServiceSpec extends Specification {

    def repositoryMock

    InvoiceService service

    void setup() {
        repositoryMock = Mock(InvoiceRepository)
        service = new InvoiceService(repository: repositoryMock)
    }

    void "Update an invoice"() {
        given: "An instance of an invoice already persisted"
        Invoice persistedInvoice = new Invoice(id: "id", number: 10, totalAmount: 100, user: new User(id: "user-1"))
        repositoryMock.getInvoiceById(_) >> persistedInvoice
        repositoryMock.saveInvoice(_) >> persistedInvoice

        and: "A map with the properties to update"
        Map properties = [id: "id", number: 11, paid: true, user: [id: "user-2"]]

        when: "The service is called to update the invoice"
        service.updateInvoice(properties)

        then: "The invoice is updated with both the original values and the new values"
        1 * repositoryMock.saveInvoice( {
            it.id == properties.id && // same in both
                    it.number == properties.number && // updated
                    it.user.id == properties.user.id && // updated
                    it.paid == properties.paid && // only in the update
                    it.totalAmount == persistedInvoice.totalAmount && // only in the old
                    it.creationDate == persistedInvoice.creationDate // only in the old (never set)
        })
    }

}
