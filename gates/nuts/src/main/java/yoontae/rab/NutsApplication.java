package yoontae.rab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NutsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NutsApplication.class);
    }
}
