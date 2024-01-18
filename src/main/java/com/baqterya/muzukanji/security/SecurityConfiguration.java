package com.baqterya.muzukanji.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String apiUri = "api/v1/kanji";
        String idApiUri = "api/v1/kanji/{kanjiId}";
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                auth -> auth
                    .requestMatchers(HttpMethod.GET, apiUri).permitAll()
                    .requestMatchers(HttpMethod.GET, idApiUri).permitAll()
                    .requestMatchers(HttpMethod.POST, apiUri).authenticated()
                    .requestMatchers(HttpMethod.PUT, idApiUri).authenticated()
                    .requestMatchers(HttpMethod.DELETE, idApiUri).authenticated()
            )
            .oauth2ResourceServer(
                oauth2 -> oauth2.jwt(
                    jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)
                )
            )
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .build();
    }
}
