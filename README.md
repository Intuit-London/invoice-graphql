# invoice-graphql
GraphQL Server for an Invoicing App.

## Overview

This is a simple server that knows how to respond to GraphQL requests for a simplice Invoicing App (Users Customers and Invoices).


It is developed as part of an [Intuit](https://www.intuit.com/) workshop to show the current frontend tech stack adopted in the company
([React](https://facebook.github.io/react/), [Relay](https://facebook.github.io/relay/) & [GraphQL](http://graphql.org/)).


The project is built using [Spring Boot](http://projects.spring.io/spring-boot/) and the [GraphQL Java Implementation](https://github.com/graphql-java/graphql-java).

## Getting started

Make sure you have installed:

* Java 1.8

To build and start the server:

```sh
$ ./gradlew build && java -jar build/libs/invoice-grapqhl-1.0-SNAPSHOT.jar
```

Run the following cURL:

```sh
$ curl -X POST -H "Content-Type:application/json"  -d '{"query":"{ user { id \n invoices { id } } }" }' http://localhost:8080/graphql
```

It should return something similar to:

```sh
{"data":{"user":{"id":"user-1","invoices":[{"id":"invoice-1"},{"id":"invoice-2"}]}}}`
```

A query of most of the schema can be seen in the [SchemaSpecFixture](src/test/groovy/com/intuit/workshop/invoicing/util/SchemaSpecFixture.groovy) class.

## License

[graphql-js License](https://github.com/graphql/graphql-js/blob/master/LICENSE)
