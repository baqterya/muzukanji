package com.baqterya.muzukanji.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAttribute;
    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;


    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(source).stream(),
                extractResourceRoles(source).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(
                source,
                authorities,
                getPrincipleClaimName(source)
        );
    }

    private String getPrincipleClaimName(Jwt source) {
        String claimName = JwtClaimNames.SUB;
        if (principleAttribute != null) {
            claimName = principleAttribute;
        }
        return source.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt source) {
        Map<String, Object> resourceAccess;
        Map<String, Object> resource;
        Collection<String> resourceRoles;

        if (source.getClaim("resource_access") == null) {
            return Set.of();
        }
        resourceAccess = source.getClaim("resource_access");
        if (resourceAccess.get(resourceId) == null) {
            return Set.of();
        }
        resource = (Map<String, Object>) resourceAccess.get(resourceId);
        resourceRoles = (Collection<String>) resource.get("roles");
        return resourceRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

}
