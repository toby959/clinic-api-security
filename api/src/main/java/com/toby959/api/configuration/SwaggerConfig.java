package com.toby959.api.configuration;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "Clinic-API-Security",
                description = "Clinic where patient and doctor data can be uploaded.",
                termsOfService = "www.toby959.com/Terms_and_services",
                version = "1.0.0",
                contact = @Contact(
                        name = "Christian Garay",
                        url = "www.tobias959.com/contact",
                        email = "www.christiangarayw8@gmail.com"
                ),
                license = @License(
                        name = "Standard Software Use License for toby959",
                        url = "www.tobias959.com/license"
                )
        ),
        servers = {
                @Server(
                        description = "DEV SERVER",
                        url = "http://localhost:8000"
                ),
                @Server(
                        description = "PROD SERVER",
                        url = "http://toby959:8080"
                )
        },
        security = @SecurityRequirement(
                name = "Security Token"
        )
)
@SecurityScheme(
        name = "Security Token",
        description = "Access Token For My API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {}
