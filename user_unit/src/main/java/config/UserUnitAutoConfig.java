package config;

import annotation.AutoConfigureUserService;
import beans.JwtService;
import beans.UserService;
import jpa.repository.RoleAssignRepository;
import jpa.repository.RoleRepository;
import jpa.repository.UserRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import redis.repository.SessionRepository;

@AutoConfiguration
@AutoConfigureUserService
@ComponentScan("beans")
public class UserUnitAutoConfig {

    @Bean
    public UserService userService(
            UserRepository userRepository
            , RoleRepository roleRepository
            , RoleAssignRepository roleAssignRepository
            , SessionRepository sessionRepository
            , JwtService jwtService
    ) {
        return new UserService(
                userRepository
                , roleRepository
                , roleAssignRepository
                , sessionRepository
                , jwtService);
    }
}
