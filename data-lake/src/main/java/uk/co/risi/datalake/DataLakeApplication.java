package uk.co.risi.datalake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataLakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataLakeApplication.class, args);
    }

    /*@Bean
    public Sampler sampler() {
        return Sampler.ALWAYS_SAMPLE;
    }*/
}
