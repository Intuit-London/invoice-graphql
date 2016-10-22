package com.intuit.workshop.invoicing.domain.entity.id

import com.intuit.workshop.invoicing.domain.entity.Customer
import com.intuit.workshop.invoicing.domain.entity.Invoice
import com.intuit.workshop.invoicing.domain.entity.InvoiceItem
import com.intuit.workshop.invoicing.domain.entity.User

enum EntityType {

    USER("/User"),
    CUSTOMER("/Customer"),
    INVOICE("/Invoice"),
    INVOICE_ITEM("/InvoiceItem")

    private String type

    static final Map<Class, EntityType> TYPES = [
            (User)       : USER,
            (Customer)   : CUSTOMER,
            (Invoice)    : INVOICE,
            (InvoiceItem): INVOICE_ITEM,
    ]

    EntityType(String type) {
        this.type = type
    }

    static EntityType fromEntity(Object entity) {
        return TYPES.get(entity.class)
    }

    static EntityType fromType(String type) {
        return values().find { it.type == type }
    }

    String toString() {
        return type
    }

}
