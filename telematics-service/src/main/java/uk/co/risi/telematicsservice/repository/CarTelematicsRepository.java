package uk.co.risi.telematicsservice.repository;

import uk.co.risi.telematicsservice.domain.CarData;
import uk.co.risi.telematicsservice.domain.TelematicsData;
import uk.co.risi.telematicsservice.domain.TelematicsIngestion;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class CarTelematicsRepository {

    private final CrudRepository crudRepository;
    private final ReactiveMongoTemplate template;

    public Mono<CarData> save(CarData carData) {
        return crudRepository.save(carData);
    }

    public Flux<CarData> findAll() {
        return crudRepository.findAll();
    }

    public Mono<UpdateResult> pushTelematicsData(TelematicsIngestion data) {
        var telematicsData = TelematicsData.convertJsonToJava(data);
        telematicsData.setTime(LocalDateTime.now());
        return template.updateFirst(
                query(where("_id").is(data.getVin())),
                new Update().push("telematicsData",
                        doc("$each", List.of(telematicsData))
                                .append("$sort", doc("time", -1))
                                .append("$slice", 20)),
                "telematicsCarData");
    }

    public Mono<TelematicsData> getLatestTelematicsData(String vin, LocalDateTime time) {
        var query = new Query(where("_id").is(vin).and("telematicsData.time").gte(time));
        query.fields()
                .exclude("_id")
                .include("telematicsData")
                .slice("telematicsData", 1);
        return template.findOne(query, CarData.class)
                .flatMap(carData -> carData.getTelematicsData().stream().findFirst()
                        .map(Mono::just).orElseGet(Mono::empty));
    }

    private Document doc(String key, Object value) {
        return new Document(key, value);
    }
}

interface CrudRepository extends ReactiveMongoRepository<CarData, String> {
}
