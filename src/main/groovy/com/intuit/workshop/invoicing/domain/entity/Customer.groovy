package com.intuit.workshop.invoicing.domain.entity

class Customer implements Entity {

    String id

    String businessName

    List<Invoice> invoices = []
}
