package com.mycelium.local.session;

import java.util.Map;

import org.reactivestreams.Publisher;

import com.mycelium.local.repository.user.UserRepo;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

import static java.util.Map.entry;

@Singleton
public class AuthenticationProviderEmailPassword implements AuthenticationProvider {

    private UserRepo userRepo;

    public AuthenticationProviderEmailPassword(UserRepo userRepo) {
        super();
        this.userRepo = userRepo;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest,
            AuthenticationRequest<?, ?> authenticationRequest) {
        return Mono.<AuthenticationResponse>create(emitter -> {
            var username = (String) authenticationRequest.getIdentity();
            var password = (String) authenticationRequest.getSecret();
            var foundUsers = this.userRepo.findByEmail(username);

            var found = false;
            for (var user : foundUsers) {
                if (password.equals(password)) {
                    emitter.success(AuthenticationResponse.success((String) authenticationRequest.getIdentity(),
                            Map.ofEntries(entry("id", user.id))));
                    found = true;
                }
            }

            if (!found) {
                emitter.error(AuthenticationResponse.exception());
            }
        });
    }

}
