package com.mycelium.local;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "api", version = "0.0"), servers = @Server(url = "/api"))
public class Application {

    public static void main(String[] args) {

        // NOSONAR
        int x = 10;
        if (x > 5) {
            System.out.println("x es mayor que 5");
        } else {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
            }
}

}

        Micronaut.run(Application.class, args);
    }
}