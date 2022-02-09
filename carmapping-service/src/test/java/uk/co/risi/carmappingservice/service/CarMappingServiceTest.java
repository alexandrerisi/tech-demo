package uk.co.risi.carmappingservice.service;

import uk.co.risi.carmappingservice.repository.CarMappingRepository;

//@SpringBootTest
class CarMappingServiceTest {

    //@Mock
    CarMappingRepository repository;

    //@InjectMocks
    CarMappingService service;

//    @Test
//    void mapIpToVin(){
//        var msg = new IncomingTelematics(50, 50, 4, "152.873.345.844");
//        var mapping = new CarMapping("152.873.345.844", "1G6DW677X60160257");
//
//        when(com.jaguarlandrover.demo.datalake.repository.findByIp(msg.getIp())).thenReturn(Mono.just(mapping));
//        service.mapIpToVin(msg).subscribe();
////    }
//
//    @Test
//    void mapInvalidIpToVin(){
//        var msg = new IncomingTelematics(50, 50, 4, "152.873.345.844");
//        when(com.jaguarlandrover.demo.datalake.repository.findByIp(msg.getIp())).thenReturn(Mono.empty());
//
//        StepVerifier.create(service.mapIpToVin(msg))
//                .expectError(IpNotFoundException.class)
//                .verify();
//    }
}