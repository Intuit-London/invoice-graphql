package com.intuit.workshop.invoicing.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class FrontController {

    @RequestMapping(value = "/")
    def @ResponseBody
    hello() {
        return "Hello"
    }

}

