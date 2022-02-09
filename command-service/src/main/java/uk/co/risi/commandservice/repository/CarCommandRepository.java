package uk.co.risi.commandservice.repository;

import uk.co.risi.commandservice.domain.CarData;
import uk.co.risi.commandservice.domain.CommandData;
import uk.co.risi.commandservice.domain.CommandIngestion;
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
public class CarCommandRepository {

    private final CrudRepository crudRepository;
    private final ReactiveMongoTemplate template;

    public Mono<CarData> save(CarData commandData) {
        return crudRepository.save(commandData);
    }

    public Flux<CarData> findAll() {
        return crudRepository.findAll();
    }

    public Mono<UpdateResult> pushCommandData(CommandIngestion data) {
        data.setTime(LocalDateTime.now());
        var commandData = CommandData.jsonToData(data);
        return template.updateFirst(
                query(where("_id").is(data.getVin())),
                new Update().push("commandData",
                        doc("$each", List.of(commandData))
                                .append("$sort", doc("time", -1))
                                .append("$slice", 20)),
                "commandCarData");
    }

    public Mono<CommandData> getLatestCommandData(String vin, LocalDateTime time) {
        var query = new Query(where("_id").is(vin).and("CommandData.time").gte(time));
        query.fields()
                .exclude("_id")
                .include("commandData")
                .slice("commandData", 1);
        return template.findOne(query, CarData.class)
                .flatMap(carData -> carData.getCommandData().stream().findFirst()
                        .map(Mono::just).orElseGet(Mono::empty));
    }

    private Document doc(String key, Object value) {
        return new Document(key, value);
    }

}

interface CrudRepository extends ReactiveMongoRepository<CarData, String> {
}
