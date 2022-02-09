package uk.co.risi.billingservice.repository;

import uk.co.risi.billingservice.domain.Bill;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class BillingRepository {

    private final ReactiveMongoTemplate template;
    private final Crud crud;

    public Mono<UpdateResult> addToBill(String vin, double amount) {
        return template.updateFirst(
                query(where("_id").is(vin)),
                new Update().inc("amount", amount),
                "bill");
    }

    public Mono<Bill> createBill(String vin) {
        return crud.save(new Bill(vin, 0));
    }

    public Flux<Bill> getAllBills() {
        return crud.findAll();
    }
}

interface Crud extends ReactiveCrudRepository<Bill, String> {
}
