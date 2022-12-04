package com.nttdata.BancaProdMovePassive.service;

import com.nttdata.BancaProdMovePassive.domain.MovePassive;
import com.nttdata.BancaProdMovePassive.domain.ProductPassive;
import com.nttdata.BancaProdMovePassive.repository.MovePassiveRepository;
import com.nttdata.BancaProdMovePassive.web.mapper.MovePassiveMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MovePassiveService {
    @Autowired
    private MovePassiveRepository movePassiveRepository;
    @Autowired
    private MovePassiveMapper movePassiveMapper;

    private final String BASE_URL = "http://localhost:9041";

    TcpClient tcpClient = TcpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 300)
            .doOnConnected(connection ->
                    connection.addHandlerLast(new ReadTimeoutHandler(3))
                            .addHandlerLast(new WriteTimeoutHandler(3)));

    private final WebClient client = WebClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
            .build();

    public Mono<ProductPassive> findProductPassiveByAccount(String account){
        return this.client.get().uri("/v1/productpassive/findByAccount/{account}",account)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(ProductPassive.class);
    }

    public Mono<ProductPassive> updateProductPassiveById(ProductPassive productPassive){
        return this.client.put().uri("/v1/productpassive/{id}",productPassive.getIdProductPassive())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(productPassive), ProductPassive.class)
                .retrieve()
                .bodyToMono(ProductPassive.class);
    }

    public Mono<ProductPassive> updateProductPassiveWebClient(ProductPassive productPassive){
        return updateProductPassiveById(productPassive);
    }

    public Flux<MovePassive> findAll(){
        log.debug("findAll executed");
        return movePassiveRepository.findAll();
    }

    public Mono<MovePassive> findById(String movepassiveId){
        log.debug("findById executed {}", movepassiveId);
        return movePassiveRepository.findById(movepassiveId);
    }


    public Mono<MovePassive> create(MovePassive movePassive){
        log.debug("create executed {}", movePassive);
        return findProductPassiveByAccount(movePassive.getAccount())
                .flatMap(productPassive1 -> {
                    if(movePassive.getOperationType().equals("ABONO")){
                        productPassive1.setAvailableAmount(productPassive1.getAvailableAmount() + movePassive.getAmount());
                    }
                    else{
                        productPassive1.setAvailableAmount(productPassive1.getAvailableAmount() - movePassive.getAmount());
                    }
                    log.debug("productpassive" +productPassive1.getIdProductPassive());
                    updateProductPassiveById(productPassive1).subscribe();
                    return movePassiveRepository.save(movePassive);
                });
    }


    public Mono<MovePassive> update(String movepassiveId,  MovePassive movePassive){
        log.debug("update executed {}:{}", movepassiveId, movePassive);
        return movePassiveRepository.findById(movepassiveId)
                .flatMap(dbMovePassive -> {
                    movePassiveMapper.update(dbMovePassive, movePassive);
                    return movePassiveRepository.save(dbMovePassive);
                });
    }


    public Mono<MovePassive> delete(String movepassiveId){
        log.debug("delete executed {}", movepassiveId);
        return movePassiveRepository.findById(movepassiveId)
                .flatMap(existingMovePassive -> movePassiveRepository.delete(existingMovePassive)
                        .then(Mono.just(existingMovePassive)));
    }


}
