package config;

import annotation.AutoUserConfigure;
import auth.JwtService;
import auth.UserService;
import user.jpa.repository.RoleAssignRepository;
import user.jpa.repository.RoleRepository;
import user.jpa.repository.UserRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import user.redis.repository.SessionRepository;

@AutoConfiguration
@AutoUserConfigure
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
