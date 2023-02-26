package com.mycelium.local;

import io.micronaut.http.annotation.Controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.http.annotation.Error;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/")
public class NotFoundController {

    @Error(status = HttpStatus.NOT_FOUND, global = true)
    public HttpResponse<JsonError> notFound(HttpRequest<String> request) {
        JsonError error = new JsonError("Page Not Found")
                .link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>notFound(error);
    }
}
