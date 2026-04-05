package com.junaid.finance_backend.config;

import com.junaid.finance_backend.security.ApiRolePolicy;
import com.junaid.finance_backend.security.JsonHttpSecurityHandlers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String[] SWAGGER_AND_DOCS = {
            "/",
            "/swagger-ui",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**"
    };

    private final JsonHttpSecurityHandlers jsonHttpSecurityHandlers;

    public SecurityConfiguration(JsonHttpSecurityHandlers jsonHttpSecurityHandlers) {
        this.jsonHttpSecurityHandlers = jsonHttpSecurityHandlers;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jsonHttpSecurityHandlers)
                        .accessDeniedHandler(jsonHttpSecurityHandlers))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_AND_DOCS).permitAll()

                        // Admin-only: user management
                        .requestMatchers("/users/**").hasRole(ApiRolePolicy.ADMIN)

                        // Records: analysts and admins read; only admins mutate
                        .requestMatchers(HttpMethod.GET, "/records/**")
                            .hasAnyRole(ApiRolePolicy.ANALYST, ApiRolePolicy.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/records").hasRole(ApiRolePolicy.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/records/**").hasRole(ApiRolePolicy.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/records/**").hasRole(ApiRolePolicy.ADMIN)

                        // Dashboard: all authenticated roles including viewer
                        .requestMatchers("/dashboard/**")
                            .hasAnyRole(ApiRolePolicy.VIEWER, ApiRolePolicy.ANALYST, ApiRolePolicy.ADMIN)

                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
