package com.jaguarlandrover.demo.billingservice.service;

import com.jaguarlandrover.demo.billingservice.domain.Bill;
import com.jaguarlandrover.demo.billingservice.repository.BillingRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository repository;

    public Mono<UpdateResult> addToBill(String vin, double amount) {
        return repository.addToBill(vin, amount)
                .flatMap(updateResult -> {
                    if (updateResult.getMatchedCount() == 0)
                        return repository.createBill(vin)
                                .flatMap(bill -> addToBill(vin, amount));
                    return Mono.just(updateResult);
                });
    }

    public Flux<Bill> retrieveAllBills() {
        return repository.getAllBills();
    }
}
