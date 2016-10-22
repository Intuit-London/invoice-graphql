package com.intuit.workshop.invoicing.domain.entity

import groovy.transform.AutoClone

@AutoClone
class Invoice implements Entity {

    String id

    Long number

    // TODO Date
    String creationDate

    // TODO Date
    String paymentDate

    Boolean paid

    BigDecimal totalAmount

    User user

    Customer customer

    List<InvoiceItem> items = []

}
