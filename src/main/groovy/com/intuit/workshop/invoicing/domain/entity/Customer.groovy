package com.intuit.workshop.invoicing.domain.entity

import groovy.transform.AutoClone

@AutoClone
class Customer implements Entity {

    String id

    String businessName

    List<Invoice> invoices = []
}
