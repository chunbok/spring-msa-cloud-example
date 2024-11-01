package yoontae.rab;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "yoontae.rab", "jpa", "redis"
})
@EntityScan(basePackages = {
        "jpa.entity", "redis.entity"
})
@EnableJpaRepositories(basePackages = {
        "jpa.repository"
})
@EnableRedisRepositories(basePackages = {
        "redis.repository"
})
@EnableDiscoveryClient
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class);
    }
}
