package uk.co.risi.gateway;

import brave.sampler.Sampler;
import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerInteger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import uk.co.risi.gateway.domain.TelematicsDecoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
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

    /**
     * Mocks OpenDS telematics data.
     * @return CommandLineRunner
     */
    @Bean
    public CommandLineRunner runner() {
        var udpPort = 12012;
        var random = ThreadLocalRandom.current();
        var runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // telematics mock
                    var data = new TelematicsDecoder();
                    data.setRpm(new BerInteger((long) ((Math.random() * ((7000 - 750) + 1)) + 750)));
                    data.setSpeed(new BerInteger((long) ((Math.random() * ((180 - 1) + 1)) + 1)));
                    data.setGear(new BerInteger((long) ((Math.random() * ((6 - 1) + 1)) + 1)));

                    var outputStream = new ReverseByteArrayOutputStream(1000);
                    data.encode(outputStream);
                    var socket = new DatagramSocket();
                    var address = InetAddress.getLoopbackAddress();
                    var packet = new DatagramPacket(
                            outputStream.getArray(),
                            outputStream.getArray().length,
                            address,
                            udpPort);
                    socket.send(packet);

                    // commands mock
                    var randomNum = random.nextInt(4);
                    String command = null;
                    if (randomNum == 0)
                        command = "brake\n";
                    else if (randomNum == 1)
                        command = "Engine on\n";
                    else if (randomNum == 2)
                        command = "Engine off\n";

                    if (command != null) {
                        packet = new DatagramPacket(command.getBytes(), command.getBytes().length, address, udpPort);
                        socket.send(packet);
                    }
                } catch (IOException ignored) {
                }
            }
        };

        return args -> Flux.interval(Duration.ofSeconds(5))
                .flatMap(aLong -> Mono.fromRunnable(runnable))
                .subscribe();
    }

    @Bean
    public Sampler sampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
