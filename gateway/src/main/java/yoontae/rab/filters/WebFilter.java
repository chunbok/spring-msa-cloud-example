package yoontae.rab.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.ArrayList;

@Configuration
@EnableWebFluxSecurity
@Slf4j
@RequiredArgsConstructor
public class WebFilter {

    private final ObjectMapper objectMapper;

    @Value("${application-info.jwt.secret}")
    private String secret;

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityWebFilterChain filterChain(
        ServerHttpSecurity http
        , AuthenticationWebFilter authenticationWebFilter
    ) throws Exception {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .cors(ServerHttpSecurity.CorsSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .addFilterBefore(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers("/auth/register", "/auth/login")
                                .permitAll()
                                .pathMatchers("/gates/nuts/**").hasAnyRole("USER")
                                .anyExchange()
                                .authenticated()
            )
            .logout(ServerHttpSecurity.LogoutSpec::disable);
        return http.build();
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter(
            ServerAuthenticationConverter serverAuthenticationConverter
            , ReactiveAuthenticationManager reactiveAuthenticationManager
    ) {

        AuthenticationWebFilter authenticationWebFilter
                = new AuthenticationWebFilter(reactiveAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(serverAuthenticationConverter);
        return authenticationWebFilter;
    }


    @Bean
    public ServerAuthenticationConverter serverAuthenticationConverter(){
        return exchange -> {
            String headerToken = exchange.getRequest().getHeaders().get("token").stream().findFirst().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No Token Found")
            );
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("", headerToken);
            return Mono.just(token);
        };
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return authentication -> {
            String headerToken = authentication.getCredentials().toString();
            Mono<Authentication> authenticationTokenMono;
            if(Strings.isBlank(headerToken)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No Token Found");
            }

            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parse(headerToken);

            PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken("user", "need?", new ArrayList<>(){{
                add(new SimpleGrantedAuthority("ROLE_USER"));
            }});
            token.setAuthenticated(true);
            authenticationTokenMono = Mono.just(token);

            return authenticationTokenMono;
        };
    }

}