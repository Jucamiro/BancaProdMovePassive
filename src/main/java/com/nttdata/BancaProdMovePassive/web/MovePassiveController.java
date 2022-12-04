package com.nttdata.BancaProdMovePassive.web;

import com.nttdata.BancaProdMovePassive.domain.MovePassive;
import com.nttdata.BancaProdMovePassive.domain.ProductPassive;
import com.nttdata.BancaProdMovePassive.repository.MovePassiveRepository;
import com.nttdata.BancaProdMovePassive.service.MovePassiveService;
import com.nttdata.BancaProdMovePassive.web.mapper.MovePassiveMapper;
import com.nttdata.BancaProdMovePassive.web.model.MovePassiveModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/movepassive")
public class MovePassiveController {
    @Value("${spring.application.name}")
    String name;

    @Value("${server.port}")
    String port;

    @Autowired
    private MovePassiveService movePassiveService;

    @Autowired
    private MovePassiveMapper movePassiveMapper;

    @GetMapping()
    public Mono<ResponseEntity<Flux<MovePassiveModel>>> getAll(){
        log.info("getAll executed");
        return Mono.just(ResponseEntity.ok()
                .body(movePassiveService.findAll()
                        .map(movePassive -> movePassiveMapper.entityToModel(movePassive))));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MovePassiveModel>> getById(@PathVariable String id){
        log.info("getById executed {}", id);
        Mono<MovePassive> response = movePassiveService.findById(id);
        return response
                .map(movePassive -> movePassiveMapper.entityToModel(movePassive))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<MovePassiveModel>> create(@Valid @RequestBody MovePassiveModel request){
        log.info("create executed {}", request);
        return movePassiveService.create(movePassiveMapper.modelToEntity(request))
                .map(moveActive -> movePassiveMapper.entityToModel(moveActive))
                .flatMap(c -> Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "moveActive", c.getIdMovePassive())))
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<MovePassiveModel>> updateById(@PathVariable String id, @Valid @RequestBody MovePassiveModel request){
        log.info("updateById executed {}:{}", id, request);
        return movePassiveService.update(id, movePassiveMapper.modelToEntity(request))
                .map(movePassive -> movePassiveMapper.entityToModel(movePassive))
                .flatMap(c -> Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "movePassive", c.getIdMovePassive())))
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id){
        log.info("deleteById executed {}", id);
        return movePassiveService.delete(id)
                .map( r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/updateProductPassiveWebClient/{id}")
    public Mono<ResponseEntity<ProductPassive>> updateProductPassiveWebClient(@PathVariable String id, @Valid @RequestBody ProductPassive request){
        log.info("updateProductPassiveWebClient executed {}:{}", id, request);
        return  movePassiveService.updateProductPassiveWebClient(request)
                .flatMap(p -> Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "movePassive", p.getIdProductPassive())))
                        .body(p)))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
