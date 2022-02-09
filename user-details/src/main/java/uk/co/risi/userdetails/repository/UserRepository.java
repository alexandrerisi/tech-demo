package uk.co.risi.userdetails.repository;

import uk.co.risi.userdetails.domain.Permission;
import uk.co.risi.userdetails.domain.User;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final ReactiveMongoTemplate template;
    private final CrudRepository crud;

    public Mono<UpdateResult> addPermission(String username, Permission permission) {
        return template.updateFirst(
                query(where("username").is(username)),
                new Update().push("permissions",
                        new Document("$each", List.of(permission))),
                "user");
    }

    public Mono<User> findByUsername(String username) {
        return crud.findByUsername(username);
    }

    public Mono<User> save(User user) {
        return crud.save(user);
    }

    public Flux<User> findAll() {
        return crud.findAll();
    }
}


interface CrudRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByUsername(String username);
}
