package config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@EntityScan(basePackages = {
        "user.jpa.entity"
})
@EnableJpaRepositories(basePackages = "user.jpa.repository")
public class JpaTestConfig {
}
