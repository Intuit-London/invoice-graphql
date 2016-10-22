package com.intuit.workshop.invoicing.domain.service

import com.intuit.workshop.invoicing.domain.entity.Invoice
import com.intuit.workshop.invoicing.domain.entity.User
import com.intuit.workshop.invoicing.domain.repository.InvoiceRepository
import spock.lang.Specification

// TODO transform to Integration Spec
class InvoiceServiceSpec extends Specification {

    def repositoryMock

    InvoiceService service

    void setup() {
        repositoryMock = Mock(InvoiceRepository)
        service = new InvoiceService(repository: repositoryMock)
    }

    void "Update an invoice"() {
        given: "An instance of an invoice already persisted"
        Invoice persistedInvoice = new Invoice(id: "id", number: 10L, totalAmount: 100, user: new User(id: "user-1"))

        and: "A map with the properties to update"
        Map properties = [id: "id", number: 11L, paid: true, user: [id: "user-2"]]

        and: "A repository"
        Invoice savedInvoice = null
        repositoryMock.getInvoiceById(_) >>> [persistedInvoice] >> { return savedInvoice }
        repositoryMock.saveInvoice(_) >> { args ->
            // HACK! Save the value for the save method for later when the second getInvoiceById is returned
            // Asserts in the end of the spec are based on this one
            savedInvoice = (Invoice)args.get(0)
            return savedInvoice
        }

        when: "The service is called to update the invoice"
        Invoice result = service.updateInvoice(properties)

        then: "The invoice is updated with both the original values and the new values"
        result.id == properties.id  // same in both
        result.number == properties.number  // updated
        result.user.id == properties.user.id  // updated
        result.paid == properties.paid  // only in the update
        result.totalAmount == persistedInvoice.totalAmount  // only in the old
        result.creationDate == persistedInvoice.creationDate // only in the old (never set)
    }

}
