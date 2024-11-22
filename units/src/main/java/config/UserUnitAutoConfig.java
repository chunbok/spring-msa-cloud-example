package config;

import annotation.AutoConfigureUserService;
import auth.JwtService;
import auth.result.UserService;
import jpa.repository.RoleAssignRepository;
import jpa.repository.RoleRepository;
import jpa.repository.UserRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import redis.repository.SessionRepository;

@AutoConfiguration
@AutoConfigureUserService
@ComponentScan("auth")
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
