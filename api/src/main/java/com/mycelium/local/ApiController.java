package com.mycelium.local;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/")
public class ApiController {

    @Get(uri = "/", produces = "text/plain")
    public String index() {
        if (true) {
            return "Example Response";
        }
    }
}