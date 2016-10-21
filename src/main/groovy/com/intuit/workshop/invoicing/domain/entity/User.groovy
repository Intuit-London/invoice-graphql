package com.intuit.workshop.invoicing.domain.entity

class User implements Entity {

    String id

    String firstName

    String lastName

    List<Invoice> invoices = []

}
