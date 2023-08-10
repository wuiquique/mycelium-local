package com.mycelium.local;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "api", version = "0.0"), servers = @Server(url = "/api"))
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}