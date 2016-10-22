package com.intuit.workshop.invoicing.domain.entity

import groovy.transform.AutoClone

@AutoClone
class User implements Entity {

    String id

    String firstName

    String lastName

    List<Invoice> invoices = []

}
