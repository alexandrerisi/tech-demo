package uk.co.risi.gateway.service;

import org.springframework.beans.factory.annotation.Qualifier;
import uk.co.risi.gateway.domain.TelematicsDecoder;
import uk.co.risi.gateway.domain.json.CanCommandJson;
import uk.co.risi.gateway.domain.json.TelematicsJson;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.udp.UdpServer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.logging.Logger;

@Service
public class ReactiveTelematicsServer {

    private WebClient.Builder lbWebClient;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private TelematicsDecoder data = new TelematicsDecoder();
    private byte[] buffer = new byte[20];

    public ReactiveTelematicsServer(@Qualifier("LbWebClient") WebClient.Builder lbWebClient) {
        this.lbWebClient = lbWebClient;
        UdpServer.create()
                .wiretap(true)
                .host("0.0.0.0")
                .handle((in, out) -> in.receiveObject()
                        .flatMap(obj -> {
                            if (obj instanceof DatagramPacket) {
                                var packet = ((DatagramPacket) obj);
                                try {
                                    for (int i = 0; i < buffer.length; i++) {
                                        byte b = packet.content().getByte(i);
                                        buffer[i] = b;
                                    }
                                    pushData(buffer, packet.sender().getAddress().getHostAddress());
                                } catch (Exception ignored) {
                                    logger.severe("Unable to push telematics data or command!");
                                }
                            }
                            return Mono.empty();
                        })).bindNow(Duration.ofSeconds(30));
    }

    private void pushData(byte[] bytes, String ipAddress) {
        Mono<Void> postAction;
        try {
            var byteArray = new ByteArrayInputStream(bytes);
            data.decode(byteArray);
            var json = new TelematicsJson(
                    data.getSpeed().intValue(),
                    data.getRpm().intValue(),
                    data.getGear().intValue(),
                    ipAddress);
            //logger.info("Publishing -> " + json);
            postAction = lbWebClient.build().post()
                    .uri("http://carmapping-service/telematics")
                    .body(Mono.just(json), TelematicsJson.class)
                    .retrieve().bodyToMono(Void.class);
        } catch (IOException e) {
            var command = new String(bytes).split("\n")[0];
            var jsonCommand = new CanCommandJson(ipAddress, command);
            postAction = lbWebClient.build().post()
                    .uri("http://carmapping-service/commands")
                    .body(Mono.just(jsonCommand), CanCommandJson.class)
                    .retrieve().bodyToMono(Void.class);
        }
        postAction.subscribe();
    }
}
