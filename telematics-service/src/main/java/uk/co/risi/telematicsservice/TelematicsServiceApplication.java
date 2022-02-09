package uk.co.risi.telematicsservice;

import brave.sampler.Sampler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class TelematicsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelematicsServiceApplication.class, args);
        //BlockHound.install();
        //Hooks.onOperatorDebug();
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
