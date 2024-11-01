package yoontae.rab.filters;

import auth.AuthInformation;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jpa.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

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
    public SecurityWebFilterChain filterChainCustom(
        ServerHttpSecurity http
        , AuthenticationWebFilter authenticationWebFilter
    ) throws Exception {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .cors(ServerHttpSecurity.CorsSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            // 직접 처리할 Oauth2 필터
            .addFilterBefore(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange(exchanges ->
                        exchanges
                                // 회원가입 로그인은 통과
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
            String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get("Authorization")).stream().findFirst().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No Authorization Found")
            );
            if(!(authHeader.contains("bearer") || authHeader.contains("Bearer"))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not bearer");
            }
            authHeader = authHeader.replace("Bearer ", "").replace("bearer ", "");

            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseClaimsJws(authHeader).getPayload();

            LocalDateTime expire = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            if(expire.isBefore(LocalDateTime.now())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expired");
            }

            String tokenType =  claims.get(AuthInformation.tokenTypeKey, String.class);
            User user = claims.get(AuthInformation.payloadYUserKey, User.class);

            PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(null, false);

            if(AuthInformation.TokenType.DIRECT.name().equals(tokenType)) {
                token = new PreAuthenticatedAuthenticationToken(tokenType, true);
            }

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
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseClaimsJws(headerToken).getPayload();

            Date expire = claims.getExpiration();

            PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken("user", "need?", new ArrayList<>(){{
                add(new SimpleGrantedAuthority("ROLE_USER"));
            }});
            token.setAuthenticated(true);
            authenticationTokenMono = Mono.just(token);

            return authenticationTokenMono;
        };
    }

}