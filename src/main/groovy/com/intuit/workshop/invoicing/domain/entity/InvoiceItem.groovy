package com.intuit.workshop.invoicing.domain.entity

import groovy.transform.AutoClone

@AutoClone
class InvoiceItem implements Entity {

    String id

    Invoice invoice

    String name

    BigDecimal price

}
