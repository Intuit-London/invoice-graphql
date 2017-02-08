package com.intuit.workshop.invoicing.graphql.fetcher

import graphql.relay.ConnectionCursor
import graphql.relay.Edge
import spock.lang.Specification
import spock.lang.Unroll

import static com.intuit.workshop.invoicing.graphql.fetcher.PaginationUtil.PaginationResult

class PaginationUtilSpec extends Specification {

    void "Passing null arguments map throws exception"() {
        when: "Paginating a null list of edges"
        PaginationUtil.paginate([], null)
        then: "An illegal argument exception is thrown"
        IllegalArgumentException exception = thrown(IllegalArgumentException)
        exception.message == "The arguments map cannot be null"
    }

    void "Passing null edges list throws exception"() {
        when: "Paginating a null list of edges"
        PaginationUtil.paginate(null, 1, 2, "3", "4")

        then: "An illegal argument exception is thrown"
        IllegalArgumentException exception = thrown(IllegalArgumentException)
        exception.message == "The list of edges cannot be null"
    }

    @Unroll("Full Pagination - [nodes:#nodes, first:#first, last:#last, after:#after, before:#before] -> [nodes:#resultNodes, hasNextPage:#hasNextPage, hasPreviousPage:#hasPreviousPage]")
    void "Cursors and slicing work as expected when used together"() {
        expect:
        PaginationResult result = PaginationUtil.paginate(toEdges(nodes), first, last, before, after)
        ArrayList<Object> paginatedNodes = result.edges*.node

        paginatedNodes == resultNodes
        result.pageInfo.hasNextPage == hasNextPage
        result.pageInfo.hasPreviousPage == hasPreviousPage

        where:
        nodes           | first | last | after | before || resultNodes | hasNextPage | hasPreviousPage
        //Empty list and no arguments provided
        []              | null  | null | null  | null   || []          | false       | false

        //Forward pagination (first + after)
        ['1', '2', '3'] | 1     | null | '1'   | null   || ['2']       | true        | false
        ['1', '2', '3'] | 2     | null | '1'   | null   || ['2', '3']  | false       | false

        //Backward pagination (last + before)
        ['1', '2', '3'] | null  | 1    | null  | '3'    || ['2']       | false       | true
        ['1', '2', '3'] | null  | 2    | null  | '3'    || ['1', '2']  | false       | false

        //Forward and backward pagination (first + last + before + after) - edge cases (discouraged by specification)
        ['1', '2', '3'] | 1     | 1    | '1'   | '3'    || ['2']       | false       | false
        ['1', '2', '3'] | 1     | 1    | '1'   | 'X'    || ['2']       | true        | true
        ['1', '2', '3'] | 1     | 1    | 'X'   | '3'    || ['1']       | true        | true
        ['1', '2', '3'] | 1     | 1    | '3'   | '1'    || []          | false       | false
    }


    @Unroll("Cursors work as expected - [nodes:#nodes, after:#after, before:#before] -> [nodes:#resultNodes]")
    void "Cursors work as expected"() {
        expect:
        PaginationResult result = PaginationUtil.paginate(toEdges(nodes), null, null, before, after)
        ArrayList<Object> paginatedNodes = result.edges*.node

        paginatedNodes == resultNodes
        !result.pageInfo.hasNextPage
        !result.pageInfo.hasPreviousPage

        where:
        nodes           | after | before || resultNodes
        //No nodes
        []              | null  | null   || []
        []              | null  | '1'    || []
        []              | '1'   | null   || []

        //Multiple nodes
        //  None/Invalid specified
        ['1', '2', '3'] | null  | null   || ['1', '2', '3']
        ['1', '2', '3'] | 'X'   | null   || ['1', '2', '3']
        ['1', '2', '3'] | null  | 'X'    || ['1', '2', '3']
        ['1', '2', '3'] | 'X'   | 'X'    || ['1', '2', '3']

        //  After specified
        ['1', '2', '3'] | '3'   | null   || []
        ['1', '2', '3'] | '2'   | null   || ['3']
        ['1', '2', '3'] | '1'   | null   || ['2', '3']

        //  Before specified
        ['1', '2', '3'] | null  | '1'    || []
        ['1', '2', '3'] | null  | '2'    || ['1']
        ['1', '2', '3'] | null  | '3'    || ['1', '2']

        //  Both before and after Specified
        ['1', '2', '3'] | '1'   | '3'    || ['2']
        ['1', '2', '3'] | '2'   | '3'    || []
        ['1', '2', '3'] | 'X'   | '3'    || ['1', '2']
    }

    @Unroll("Slicing works as expected - [nodes:#nodes, first:#first, last:#last] -> [nodes:#resultNodes, hasNextPage:#hasNextPage, hasPreviousPage:#hasPreviousPage]")
    void "Slicing works as expected"() {
        expect:
        PaginationResult result = PaginationUtil.paginate(toEdges(nodes), first, last, null, null)
        ArrayList<Object> paginatedNodes = result.edges*.node

        paginatedNodes == resultNodes
        result.pageInfo.hasNextPage == hasNextPage
        result.pageInfo.hasPreviousPage == hasPreviousPage

        where:
        nodes      | first | last || resultNodes | hasNextPage | hasPreviousPage
        //Zero Nodes
        []         | 1     | null || []          | false       | false
        []         | null  | 1    || []          | false       | false

        //Multiple Nodes
        //None specified
        ['1', '2'] | null  | null || ['1', '2']  | false       | false

        //  First Specified
        ['1', '2'] | 0     | null || []          | true        | false
        ['1', '2'] | 1     | null || ['1']       | true        | false
        ['1', '2'] | 2     | null || ['1', '2']  | false       | false
        ['1', '2'] | 3     | null || ['1', '2']  | false       | false

        //  Last specified
        ['1', '2'] | null  | 0    || []          | false       | true
        ['1', '2'] | null  | 1    || ['2']       | false       | true
        ['1', '2'] | null  | 2    || ['1', '2']  | false       | false
        ['1', '2'] | null  | 3    || ['1', '2']  | false       | false

        //  First and Last specified - edge cases discouraged by specification
        ['1', '2'] | 0     | 2    || []          | true        | true
        ['1', '2'] | 1     | 2    || ['1']       | true        | true
        ['1', '2'] | 2     | 2    || ['1', '2']  | false       | false
        ['1', '2'] | 3     | 2    || ['1', '2']  | false       | false
        ['1', '2'] | 2     | 3    || ['1', '2']  | false       | false
        ['1', '2'] | 2     | 1    || ['2']       | true        | true
    }

    static List<Edge> toEdges(List<String> list) {
        return list?.collect { new Edge(it, new ConnectionCursor(it)) }
    }
}
