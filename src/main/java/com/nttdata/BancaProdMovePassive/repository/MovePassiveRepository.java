package com.nttdata.BancaProdMovePassive.repository;

import com.nttdata.BancaProdMovePassive.domain.MovePassive;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MovePassiveRepository extends ReactiveMongoRepository<MovePassive, String> {
    Mono<MovePassive> findByAccount(String account);
}
