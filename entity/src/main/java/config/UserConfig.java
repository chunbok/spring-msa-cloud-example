package config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@AutoConfiguration
@EntityScan(basePackages = {
        "user.jpa.entity"
        , "user.redis.entity"
})
@EnableJpaRepositories(basePackages = "user.jpa.repository")
@EnableRedisRepositories(basePackages = "user.redis.repository")
public class UserConfig {

}
