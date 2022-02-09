package uk.co.risi.billingservice.api;

import uk.co.risi.billingservice.domain.Bill;
import uk.co.risi.billingservice.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private final BillingService service;

    @PutMapping("/{vin}/{amount}")
    public Mono<Void> addToBill(@PathVariable String vin, @PathVariable double amount) {
        return service.addToBill(vin, amount).then();
    }

    @GetMapping("/bills")
    public Flux<Bill> retrieveAllBills() {
        return service.retrieveAllBills();
    }
}
