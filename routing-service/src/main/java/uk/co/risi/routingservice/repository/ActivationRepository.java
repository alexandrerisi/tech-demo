package uk.co.risi.routingservice.repository;

import uk.co.risi.routingservice.domain.Activation;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class ActivationRepository {

    private final Crud crud;
    private final ReactiveMongoTemplate template;

    public Mono<UpdateResult> updateActivations(String vin, boolean isTelematics, boolean isIncrement) {
        return template.updateFirst(query(where("_id").is(vin)),
                new Update().inc((isTelematics ? "telematicsActivations" : "commandsActivations"),
                        isIncrement ? 1 : -1),
                "activation");
    }

    public Mono<Activation> save(Activation activation) {
        return crud.save(activation);
    }

    public Mono<Activation> findById(String vin) {
        return crud.findById(vin);
    }

    public Flux<Activation> findAll() {
        return crud.findAll();
    }
}

interface Crud extends ReactiveMongoRepository<Activation, String> {
}
