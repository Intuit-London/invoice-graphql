package com.intuit.workshop.invoicing.graphql

import com.intuit.workshop.invoicing.graphql.introspection.IntrospectionQuery
import com.intuit.workshop.invoicing.graphql.schema.GraphQLSchemaHolder
import graphql.ExecutionResult
import graphql.GraphQL
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

@Slf4j
@Service
class GraphQLExecutionService {

    private GraphQL graphQL

    @Autowired
    GraphQLSchemaHolder graphQLSchemaHolder

    @PostConstruct
    void init() {
        graphQL = new GraphQL(graphQLSchemaHolder.graphQLSchema)
    }

    Map<String, Object> execute(String query, Map<String, Object> variables = [:]) {
        ExecutionResult executionResult = graphQL.execute(query, (Object) null, variables ?: [:]);
        Map<String, Object> result = new LinkedHashMap<>();
        if (executionResult.getErrors().size() > 0) {
            result.put("errors", executionResult.getErrors());
            log.error("Errors: {}", executionResult.getErrors());
        }
        result.put("data", executionResult.getData());
        return result;
    }

    Map<String, Object> executeIntrospection() {
        return execute(IntrospectionQuery.QUERY)
    }


}
