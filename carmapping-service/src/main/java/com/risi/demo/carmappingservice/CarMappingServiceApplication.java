package com.risi.demo.carmappingservice;

import brave.sampler.Sampler;
import com.risi.demo.carmappingservice.domain.CarMapping;
import com.risi.demo.carmappingservice.repository.CarMappingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CarMappingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarMappingServiceApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder lbWebClient() {
        return WebClient.builder();
    }

    @Bean
    public CommandLineRunner runner(CarMappingRepository repository){
        return args -> {
            // For Gateway Mock
            var vin = "1G6DW677X60160257";
            var mapping = new CarMapping("127.0.0.1", vin);
            repository.save(mapping).subscribe();
        };
    }

    @Bean
    public Sampler sampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
