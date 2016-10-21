package com.intuit.workshop.invoicing.domain.service

import com.intuit.workshop.invoicing.domain.entity.Invoice
import com.intuit.workshop.invoicing.domain.repository.InvoiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class InvoiceService {

    @Autowired
    InvoiceRepository repository

    Invoice createInvoice(Invoice invoice) {
        return repository.saveInvoice(invoice)
    }

}
