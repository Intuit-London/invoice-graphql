package com.intuit.workshop.invoicing.web

import com.intuit.workshop.invoicing.graphql.service.GraphQLExecutionService
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class FrontController {

    @Autowired
    GraphQLExecutionService graphQLExecutionService

    @CrossOrigin
    @RequestMapping(value = "/ping")
    def @ResponseBody
    ping() {
        return "Ok"
    }

    @CrossOrigin
    @RequestMapping(
            value = "/graphql",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    Object execute(@RequestBody Map body) {
        String query = (String) body.get("query")
        Map<String, Object> variables = getVariablesFromRequest(body)
        return graphQLExecutionService.execute(query, variables);
    }

    @CrossOrigin
    @RequestMapping(
            value = "/introspection",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    Object executeIntrospection() {
        return graphQLExecutionService.executeIntrospection()
    }

    private static Map<String, Object> getVariablesFromRequest(Map body) {
        Object variables = body.get("variables")
        if (variables instanceof HashMap) {
            return (Map<String, Object>)variables
        }
        if (variables instanceof String && variables != "") {
            return (Map<String, Object>) new JsonSlurper().parseText(variables)
        }
        return null
    }

}

