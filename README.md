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
$ curl -X POST -H "Content-Type:application/json"  -d '{"query":"{ users { id \n invoices { id } } }" }' http://localhost:8080/graphql
```

It should return something similar to:

```sh
{"data":{"users":[{"id":"L1VzZXI6dXNlci0x","invoices":[{"id":"L0ludm9pY2U6aW52b2ljZS0x"},{"id":"L0ludm9pY2U6aW52b2ljZS0y"}]}]}}
```

## Full Query

The server comes with some static data loaded on bootstrap, which allows to try queries right away.

For instance, the following query (in any GraphiQL-like extension as [ChromeiQL](https://chrome.google.com/webstore/detail/chromeiql/fkkiamalmpiidkljmicmjfbieiclmeij)):

```
{
    users {
        id
        firstName
        lastName
        invoices {
            id
            user {
                id
            }
            number
            customer {
                id
                businessName
                invoices {
                    id
                }
            }
            creationDate
            paymentDate
            paid
            items {
                id
                invoice {
                    id
                }
                name
                price
            }
            totalAmount
        }
    }
}
```

will return something like:

```
{
  "data": {
    "users": [
      {
        "id": "L1VzZXI6dXNlci0x",
        "firstName": "Alexander",
        "lastName": "Fleming",
        "invoices": [
          {
            "id": "L0ludm9pY2U6aW52b2ljZS0x",
            "user": {
              "id": "L1VzZXI6dXNlci0x"
            },
            "number": 1,
            "customer": {
              "id": "L0N1c3RvbWVyOmN1c3RvbWVyLTE=",
              "businessName": "Erns Boris Chain",
              "invoices": [
                {
                  "id": "L0ludm9pY2U6aW52b2ljZS0x"
                }
              ]
            },
            "creationDate": "Sat Oct 01 00:00:00 BST 1927",
            "paymentDate": "Sat Oct 01 00:00:00 BST 1927",
            "paid": true,
            "items": [
              {
                "id": "L0ludm9pY2VJdGVtOmludm9pY2UtMS1pdGVtLTE=",
                "invoice": {
                  "id": "L0ludm9pY2U6aW52b2ljZS0x"
                },
                "name": "Petri Dishes",
                "price": 100
              },
              {
                "id": "L0ludm9pY2VJdGVtOmludm9pY2UtMS1pdGVtLTI=",
                "invoice": {
                  "id": "L0ludm9pY2U6aW52b2ljZS0x"
                },
                "name": "Gloves",
                "price": 200
              }
            ],
            "totalAmount": 300
          },
          {
            "id": "L0ludm9pY2U6aW52b2ljZS0y",
            "user": {
              "id": "L1VzZXI6dXNlci0x"
            },
            "number": 2,
            "customer": {
              "id": "L0N1c3RvbWVyOmN1c3RvbWVyLTI=",
              "businessName": "Howard Florey",
              "invoices": [
                {
                  "id": "L0ludm9pY2U6aW52b2ljZS0y"
                }
              ]
            },
            "creationDate": "Sat Oct 01 00:00:00 BST 1927",
            "paymentDate": null,
            "paid": false,
            "items": [
              {
                "id": "L0ludm9pY2VJdGVtOmludm9pY2UtMi1pdGVtLTE=",
                "invoice": {
                  "id": "L0ludm9pY2U6aW52b2ljZS0y"
                },
                "name": "Staphylococcus plate culture",
                "price": 50
              }
            ],
            "totalAmount": 50
          }
        ]
      }
    ]
  }
}
```

A query with most of the schema can be seen in the [SchemaSpecFixture](src/test/groovy/com/intuit/workshop/invoicing/util/SchemaSpecFixture.groovy) class.

## Docker Image

The project comes with ``Dockerfile`` file describing an image to run the application.

To build a locally tagged image:

```sh
$ ./gradlew build docker
```

To run a container with the image in local (you need to have [Docker](https://www.docker.com/) installed and running in your laptop):

```sh
$ docker run -p 8081:8080 -t com.intuit.workshop/invoice-grapqhl
```

Now the application is running in ``http://localhost:8081/graphql``:

```sh
$ curl -X POST -H "Content-Type:application/json"  -d '{"query":"{ users { id \n invoices { id } } }" }' http://localhost:8081/graphql
{"data":{"users":[{"id":"L1VzZXI6dXNlci0x","invoices":[{"id":"L0ludm9pY2U6aW52b2ljZS0x"},{"id":"L0ludm9pY2U6aW52b2ljZS0y"}]}]}}
```

To list the containers running:

```sh
$ docker ps
CONTAINER ID        IMAGE                                 COMMAND                CREATED             STATUS              PORTS                    NAMES
53e7c0f2233f        com.intuit.workshop/invoice-grapqhl   "java -jar /app.jar"   7 minutes ago       Up 7 minutes        0.0.0.0:8081->8080/tcp   ***
```


## References

- [GraphQL Official Site](http://graphql.org/)
- [GraphQL Spec](https://facebook.github.io/graphql/)
- [GraphQL Reference Implementation for JavaScript](https://github.com/graphql/graphql-js)
- [Awesome GraphQl](https://github.com/chentsulin/awesome-graphql)
- [GraphQL Java Library](https://github.com/graphql-java/graphql-java)

## License

[graphql-js License](https://github.com/graphql/graphql-js/blob/master/LICENSE)
