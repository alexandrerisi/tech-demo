package uk.co.risi.routingservice;

import brave.sampler.Sampler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class RoutingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoutingServiceApplication.class, args);
    }

    @Bean("LbWebClient")
    @LoadBalanced
    public WebClient.Builder lbWebClient() {
        return WebClient.builder();
    }

    @Bean
    @Qualifier
    @Primary
    public WebClient.Builder webClient() {
        return WebClient.builder();
    }

    @Bean
    public Sampler sampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
