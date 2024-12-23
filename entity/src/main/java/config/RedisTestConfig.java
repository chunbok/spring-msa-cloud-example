package config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@AutoConfiguration
@EntityScan(basePackages = {
        "user.redis.entity"
})
@EnableRedisRepositories(basePackages = "user.redis.repository")
public class RedisTestConfig {
}
