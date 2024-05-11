package yoontae.rab.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Configuration
@EnableWebFluxSecurity
@Slf4j
@RequiredArgsConstructor
public class WebFilter {

    private final ObjectMapper objectMapper;

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
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("ee", "ee");
        return exchange -> {
            return Mono.just(token);
        };

    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return authentication -> {
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();
            Mono<Authentication> authenticationTokenMono;
            if(Strings.isBlank(username) || Strings.isBlank(password)) {
                PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken("","");
                token.setAuthenticated(false);
                authenticationTokenMono = Mono.just(token);
            }
            else if(!(username.equals("ee") && password.equals("ee"))) {
                PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken("","");
                token.setAuthenticated(false);
                authenticationTokenMono = Mono.just(token);
            } else {
                PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken("user", "need?", new ArrayList<>(){{
                    add(new SimpleGrantedAuthority("ROLE_USER"));
                }});
                token.setAuthenticated(true);
                authenticationTokenMono = Mono.just(token);
            }

            return authenticationTokenMono;
        };
    }

}