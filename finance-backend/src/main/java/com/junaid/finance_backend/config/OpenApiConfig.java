package com.junaid.finance_backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BASIC = "basicAuth";

    @Bean
    public OpenAPI financeBackendOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Finance Backend API")
                        .description("""
                                **Authentication is enabled:** HTTP Basic. In Swagger, use **Authorize** \
                                (lock icon): **Username** = user email (e.g. admin@finance.local); \
                                **Password** = your account password (seed data uses the plaintext `password`).

                                **Roles:** VIEWER — dashboard only. ANALYST — dashboard + read records. \
                                ADMIN — user management + full record CRUD.

                                OpenAPI and Swagger UI paths are public; all other endpoints require a valid user.""")
                        .version("v1"))
                .addSecurityItem(new SecurityRequirement().addList(BASIC))
                .components(new Components()
                        .addSecuritySchemes(BASIC,
                                new SecurityScheme()
                                        .name(BASIC)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")));
    }
}
