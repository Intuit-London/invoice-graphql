package com.intuit.workshop.invoicing.domain.entity

class Invoice implements Entity {

    public String id

    public Long number

    // TODO Date
    public String creationDate

    // TODO Date
    public String paymentDate

    public Boolean paid

    // TODO Amount
    public Integer totalAmount

    public User user

    public Customer customer

    public List<InvoiceItem> items

}
