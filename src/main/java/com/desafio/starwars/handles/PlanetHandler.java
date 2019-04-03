package com.desafio.starwars.handles;

import com.desafio.starwars.entity.PlanetEntity;
import com.desafio.starwars.repositories.ReactivePlanetRepository;
import com.desafio.starwars.webclient.PlanetClient;
import dto.PlanetApiDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Component
public class PlanetHandler {

    @Autowired
    private transient ReactivePlanetRepository reactivePlanetRepository;

    private transient PlanetClient planetClient = new PlanetClient();

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanetHandler.class);

    public Mono<ServerResponse> allPlanetApi(ServerRequest req) {
        return ok().body(this.planetClient.getApiAllPlanets(), PlanetApiDto.class);
    }

    public Mono<ServerResponse> all(ServerRequest req) {
        return ok().contentType(APPLICATION_JSON)
                .body(this.reactivePlanetRepository.findAll(), PlanetEntity.class)
                .doOnError(throwable -> new IllegalStateException("My godness! NOOOOOOOOOO!!"));
    }

    public Mono<ServerResponse> findById(ServerRequest req) {
        final UUID id = UUID.fromString(req.pathVariable("id"));
        final Mono<PlanetEntity> planet = this.reactivePlanetRepository.findById(id);
        return planet
                .flatMap(p -> ok().contentType(MediaType.APPLICATION_JSON).body(fromPublisher(planet, PlanetEntity.class)))
                .switchIfEmpty(notFound().build()) // or Mono.error()
                .log()
                .doOnError(throwable -> new IllegalStateException("Noooo! Not againnnnnn =("));
    }

    public Mono<ServerResponse> findByName(ServerRequest req) {
        String name = req.pathVariable("name");
        return ok().contentType(APPLICATION_JSON)
                .body(fromPublisher(
                        this.reactivePlanetRepository.findAllByName(name),
                        PlanetEntity.class)
                )
                .doOnError(throwable -> new IllegalStateException("I give up!!!"));
    }

    public Mono<ServerResponse> create(ServerRequest req) {
        return req.bodyToMono(PlanetEntity.class)
                .flatMap(planetEntity ->
                        this.reactivePlanetRepository.findAllByName(planetEntity.getName())
                            .map(planetExist -> {
                                LOGGER.info("Planet already exist {}", planetExist.getName());
                                return planetExist;
                            })
                            .switchIfEmpty(Mono.fromSupplier(() -> {
                                final String namePlanet = planetEntity.getName();
                                LOGGER.info("create new planet {}", planetEntity.getName());
                                return planetEntity;
                            }))
                            .flatMap(uuid -> this.reactivePlanetRepository.save(uuid))

                )
                .flatMap(uuid -> ServerResponse.ok().contentType(APPLICATION_JSON).syncBody(uuid.getId()));
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
        final UUID id = UUID.fromString(req.pathVariable("id"));
        return this.reactivePlanetRepository
                .findById(id)
                .flatMap(p -> noContent().build(this.reactivePlanetRepository.delete(p)))
                .switchIfEmpty(notFound().build());
    }

}
