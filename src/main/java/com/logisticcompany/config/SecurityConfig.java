package com.logisticcompany.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtDecoder jwtDecoder;

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakAuthorityConverter());
        return converter;
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();

        return userRequest -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);
            OAuth2AccessToken accessToken = userRequest.getAccessToken();

            Set<GrantedAuthority> mappedAuthorities = new HashSet<>(oidcUser.getAuthorities());

            if (accessToken != null && accessToken.getTokenValue() != null) {
                try {
                    Jwt accessTokenJwt = jwtDecoder.decode(accessToken.getTokenValue());

                    Map<String, Object> realmAccess = accessTokenJwt.getClaimAsMap("realm_access");
                    if (realmAccess != null && realmAccess.containsKey("roles")) {
                        List<String> realmRoles = (List<String>) realmAccess.get("roles");
                        if (realmRoles != null) {
                            mappedAuthorities.addAll(
                                    realmRoles.stream()
                                            .map(SimpleGrantedAuthority::new)
                                            .collect(Collectors.toSet())
                            );
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Error decoding access token in oidcUserService: " + e.getMessage());
                }
            }

            return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/register/**").permitAll()
                        .requestMatchers("/api/shipments/**").hasAnyRole("CLIENT", "OFFICE_EMPLOYEE", "COURIER")
                        .requestMatchers("/api/clients/**", "/api/offices/**", "/api/revenue/**", "/revenue/**",
                                "/api/office-employees/**", "/api/couriers/**")
                        .hasRole("OFFICE_EMPLOYEE")
                        .requestMatchers("/api/shipments/assigned", "/courier/**").hasRole("COURIER")
                        .requestMatchers("/api/shipments/mine").hasRole("CLIENT")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(oidcUserService())
                        )
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("http://localhost:8081/realms/logistics-app/protocol/openid-connect/logout?redirect_uri=http://localhost:8080/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}