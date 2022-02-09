package uk.co.risi.carmappingservice.service;

import uk.co.risi.carmappingservice.config.CounterProperties;
import uk.co.risi.carmappingservice.repository.CarMappingRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@EnableAsync
@Configuration
@EnableScheduling
public class CacheService {

    private Logger logger = Logger.getLogger(getClass().getName());
    private final CounterProperties counterProperties;
    private final CarMappingRepository repository;
    private final long IP_CLEAN_FIXED_DELAY = 200000;

    public Map<String, CarBinding> cacheIpMap = new HashMap<>();


    /**
     * Clears the ip/vin cache if the property is not being used anymore.
     */
    @Async
    @Scheduled(fixedDelay = IP_CLEAN_FIXED_DELAY)
    protected void ipCacheMappingClean() {
        logger.info("Ip map scheduler running...");
        //maps ip to vin
        cacheIpMap.forEach((ip, carBinding) -> {
            var counter = carBinding.getCounter();
            if (counter <= 1)
                cacheIpMap.remove(ip);
            else if (counter >= 100)
                carBinding.setCounter(100);
            else
                carBinding.setCounter(counter - 25);
        });
    }

    /**
     * Checks if the ip/vin is in the Map, if not queries the database and updates the cache.
     * The first request will comeback as null.
     *
     * @param ip Ip of the car.
     * @return vin of the car.
     */
    public String getVinForIp(String ip) {
        var binding = cacheIpMap.get(ip);
        if (binding != null) {
            binding.setCounter(binding.getCounter() + 1);
            return binding.getVin();
        } else {
            repository.findByIp(ip).map(carMapping -> {
                cacheIpMap.putIfAbsent(ip, new CarBinding(carMapping.getVin()));
                return carMapping;
            }).subscribe();
        }
        return null;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    class CarBinding {
        private final String vin;
        // Determines when the binding will be removed from the cache.
        private int counter = counterProperties.getCarbindingCounter();
    }
}


