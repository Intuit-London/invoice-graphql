package com.intuit.workshop.invoicing.domain.service

import com.intuit.workshop.invoicing.domain.entity.Invoice
import com.intuit.workshop.invoicing.domain.repository.InvoiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.Assert

@Component
class InvoiceService {

    @Autowired
    InvoiceRepository repository

    Invoice createInvoice(Map properties) {
        Invoice invoice = repository.saveInvoice(new Invoice(properties))
        return repository.getInvoiceById(invoice.id)
    }

    Invoice updateInvoice(Map properties) {
        Invoice persistedInvoice = repository.getInvoiceById(properties.id).clone()
        Assert.notNull(persistedInvoice, "Can not find the invoice to update (ID: ${properties.id})")
        merge(properties, persistedInvoice)
        Invoice invoice = repository.saveInvoice(persistedInvoice)
        repository.getInvoiceById(invoice)
    }

    /**
     * Merges all properties available in the source Map into the target Object
     *
     * Note that the Map needs to have the same structure as the object.
     *
     * @param source
     * @param target
     */
    private void merge(Map<String, Object> source, Object target) {
        source.each { String key, Object value ->
            if (!(value instanceof Map)) {
                target."$key" = value
            } else {
                merge((Map)source."$key", target."$key")
            }
        }

    }

}
