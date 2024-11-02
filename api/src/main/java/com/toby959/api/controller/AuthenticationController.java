package com.toby959.api.controller;

import com.toby959.api.domain.users.DataAuthenticateUser;
import com.toby959.api.domain.users.User;
import com.toby959.api.infra.security.DataJWTToken;
import com.toby959.api.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Authentication", description = "Controller for Authentication")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final TokenService service;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService service) {
        this.authenticationManager = authenticationManager;
        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "Login User",
            description = "Authenticate a user and return the authentication token along with user details.",
            tags = {"Authentication"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication request with username and password",
                    required = true,
                    content = @Content(
                           mediaType = "application/json",
                            schema = @Schema(implementation = DataAuthenticateUser.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful authentication",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DataJWTToken.class)
                            )
                    )
            }
    )
    public ResponseEntity<DataJWTToken> authenticateUser(@RequestBody @Valid DataAuthenticateUser dataAuthenticateUser) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(dataAuthenticateUser.login(),
                dataAuthenticateUser.user_key());
        var authenticatedUser = authenticationManager.authenticate(authToken);
        var JWTtoken = service.generateToken((User) authenticatedUser.getPrincipal());
        return ResponseEntity.ok(new DataJWTToken(JWTtoken));
    }
}