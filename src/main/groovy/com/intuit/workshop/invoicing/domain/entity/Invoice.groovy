package com.intuit.workshop.invoicing.domain.entity

class Invoice implements Entity {

    String id

    Long number

    // TODO Date
    String creationDate

    // TODO Date
    String paymentDate

    Boolean paid

    // TODO Amount
    Integer totalAmount

    User user

    Customer customer

    List<InvoiceItem> items = []

}
