package com.intuit.workshop.invoice

import com.intuit.workshop.invoice.model.InvoiceRootQuery
import graphql.GraphQL
import graphql.annotations.GraphQLAnnotations
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import spock.lang.Ignore
import spock.lang.Specification

class InvoiceSchemaSpec extends Specification {

    private final static String query =
"""
{
    invoice {
        id
        number
        items {
            invoice {
                id
            }
            price
        }
    }
}
"""

    void "Querying an Invoice with their items and parent ref brings the parent Invoice"() {
        given:
        GraphQLObjectType queryType = GraphQLAnnotations.object(InvoiceRootQuery.class);

        GraphQLSchema schema = GraphQLSchema.newSchema()
                .query(queryType)
                .build();

        expect:
        Map<String, Object> result = new GraphQL(schema).execute(query).getData();
        result == [
                invoice:
                        [
                                id: "100",
                                number: 5,
                                items: [
                                        [
                                                invoice: [
                                                        id: "100"
                                                ],
                                                price  : 100
                                        ]
                                ]
                        ]
        ]
    }

    @Ignore
    void "Building the schema with the Builder API"() {
//        DataFetcher dataFetcher = new MyDataFetcher()
//
//        GraphQLObjectType invoiceType = newObject()
//            .name("Invoice")
//            .field(
//                newFieldDefinition()
//                    .name("id")
//                    .type(GraphQLString).build())
//            .field(
//                newFieldDefinition()
//                    .name("number")
//                    .type(GraphQLLong).build())
//            .build()
//
//        GraphQLObjectType queryType = newObject()
//                .name("invoiceMainQuery")
//                .field(
//                newFieldDefinition()
//                        .type(invoiceType)
//                        .name("invoice")
//                        .dataFetcher(dataFetcher)
////                        .staticValue(new Invoice(id: "100", number: 100))
//                        .build())
//                .build();
    }

}


