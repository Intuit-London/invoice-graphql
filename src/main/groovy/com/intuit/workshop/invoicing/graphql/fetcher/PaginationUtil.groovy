package com.intuit.workshop.invoicing.graphql.fetcher

import graphql.relay.Edge
import graphql.relay.PageInfo

class PaginationUtil {

    static PaginationResult paginate(List<Edge> edges, Map arguments) {
        if(arguments == null){
            throw new IllegalArgumentException("The arguments map cannot be null")
        }

        String before = arguments.before as String
        String after = arguments.after as String
        Integer first = arguments.first as Integer
        Integer last = arguments.last as Integer

        return paginate(edges, first, last, before, after)
    }

    static PaginationResult paginate(List<Edge> edges, Integer first, Integer last, String before, String after) {
        if(edges == null){
            throw new IllegalArgumentException("The list of edges cannot be null")
        }

        List<Edge> paginatedEdges = applyCursorsToEdges(edges, before, after)
        List<Edge> slicedEdges = edgesToReturn(paginatedEdges, first, last)

        boolean hasPreviousPage = last != null && slicedEdges.size() < paginatedEdges.size()
        boolean hasNextPage = first != null && slicedEdges.size() < paginatedEdges.size()
        PageInfo pageInfo = new PageInfo(hasNextPage: hasNextPage, hasPreviousPage: hasPreviousPage)

        return new PaginationResult(edges: slicedEdges, pageInfo: pageInfo)
    }

    private static List<Edge> applyCursorsToEdges(List<Edge> allEdges, String beforeCursor, String afterCursor) {
        List<Edge> edges = allEdges

        if (afterCursor != null) {
            boolean afterCursorExistsInEdges = edges.any { (it.cursor.value == afterCursor) }
            if (afterCursorExistsInEdges) {
                edges = edges.dropWhile { Edge e -> e.cursor.value != afterCursor }.drop(1)
            }
        }

        if (beforeCursor != null) {
            boolean beforeCursorExistsInEdges = edges.any { (it.cursor.value == beforeCursor) }
            if (beforeCursorExistsInEdges) {
                edges = edges.takeWhile { Edge e -> e.cursor.value != beforeCursor }
            }
        }

        return edges
    }

    private static List<Edge> edgesToReturn(List<Edge> edges, Integer first, Integer last) {
        if (first != null) {
            if (first < 0) {
                throw new Exception("Value of parameter 'first' must be equal to or higher than 0")
            }

            edges = edges.take(first)
        }

        if (last != null) {
            if (last < 0) {
                throw new Exception("Value of parameter 'last' must be equal to or higher than 0")
            }

            edges = edges.takeRight(last)
        }

        return edges
    }

    static class PaginationResult {
        List<Edge> edges
        PageInfo pageInfo
    }
}
