package config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@AutoConfiguration
@EntityScan(basePackages = {
        "jpa.entity"
        , "redis.entity"
})
@EnableJpaRepositories(basePackages = "jpa.repository")
@EnableRedisRepositories(basePackages = "redis.repository")
public class RepositoryConfig {

}
