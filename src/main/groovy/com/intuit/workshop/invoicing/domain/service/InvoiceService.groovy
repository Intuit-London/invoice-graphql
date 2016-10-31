package com.intuit.workshop.invoicing.domain.service

import com.intuit.workshop.invoicing.domain.entity.Invoice
import com.intuit.workshop.invoicing.domain.entity.InvoiceItem
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
        Invoice persistedInvoice = (Invoice)repository.getInvoiceById(properties.id)?.clone()
        Assert.notNull(persistedInvoice, "Can not find the invoice to update (ID: ${properties.id})")
        merge(properties, persistedInvoice)
        Invoice invoice = repository.saveInvoice(persistedInvoice)
        return repository.getInvoiceById(invoice.id)
    }

    InvoiceItem createInvoiceItem(Map properties) {
        InvoiceItem invoiceItem = repository.saveInvoiceItem(new InvoiceItem(properties))
        return repository.getInvoiceItemById(invoiceItem.id)
    }

    InvoiceItem updateInvoiceItem(Map properties) {
        InvoiceItem persistedInvoiceItem = (InvoiceItem)repository.getInvoiceItemById(properties.id)?.clone()
        Assert.notNull(persistedInvoiceItem, "Can not find the invoice item to update (ID: ${properties.id})")
        merge(properties, persistedInvoiceItem)
        InvoiceItem invoiceItem = repository.saveInvoiceItem(persistedInvoiceItem)
        return repository.getInvoiceItemById(invoiceItem.id)
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
