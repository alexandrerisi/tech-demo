package uk.co.risi.gateway.service;

//@Service
public class TelematicsServer {
    /*@Autowired
    private WebClient.Builder lbWebClient;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private DatagramSocket socket = new DatagramSocket(12012);
    private byte[] buffer = new byte[256];
    private static TelematicsData data = new TelematicsData();

    public TelematicsServer() throws SocketException {
        Runnable runnable = () -> {
            while (true) {
                var packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(packet);
                    pushData(packet.getData(), packet.getAddress().getHostAddress());
                } catch (Exception ignored) {
                    logger.severe("Unable to push telematics data or command!");
                }
            }
        };
        Mono.fromRunnable(runnable)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    private void pushData(byte[] bytes, String ipAddress) {
        Mono<Void> postAction;
        try {
            var byteArray = new ByteArrayInputStream(bytes);
            data.decode(byteArray);
            var json = new JsonTelematics(
                    data.getSpeed().intValue(),
                    data.getRpm().intValue(),
                    data.getGear().intValue(),
                    ipAddress);
            postAction = lbWebClient.build().post()
                    .uri("http://carmapping-service/telematics")
                    .body(Mono.just(json), JsonTelematics.class)
                    .retrieve().bodyToMono(Void.class);
            System.out.println(json);
        } catch (IOException e) {
            var command = new String(bytes).split("\n")[0];
            var jsonCommand = new CanCommand(ipAddress, command);
            postAction = lbWebClient.build().post()
                    .uri("http://carmapping-service/commands")
                    .body(Mono.just(jsonCommand), CanCommand.class)
                    .retrieve().bodyToMono(Void.class);
            System.out.println(jsonCommand);
        }
        postAction.block();
    }*/

}
