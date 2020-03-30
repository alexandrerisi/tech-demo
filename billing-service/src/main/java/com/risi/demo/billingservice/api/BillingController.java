package com.risi.demo.billingservice.api;

import com.risi.demo.billingservice.domain.Bill;
import com.risi.demo.billingservice.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private final BillingService service;

    @PutMapping("${ep.billing.add}")
    public Mono<Void> addToBill(@PathVariable String vin, @PathVariable double amount) {
        return service.addToBill(vin, amount).then();
    }

    @GetMapping("${ep.billing.bills}")
    public Flux<Bill> retrieveAllBills() {
        return service.retrieveAllBills();
    }
}
