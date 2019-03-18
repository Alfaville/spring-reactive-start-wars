package com.desafio.starwars.handles;

import com.desafio.starwars.entity.PlanetEntity;
import com.desafio.starwars.repositories.ReactivePlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

import static com.desafio.starwars.configurations.Router.RouterUtils.PlanetRoute;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Component
public class PlanetHandler {

    @Autowired
    private ReactivePlanetRepository reactivePlanetRepository;

    public Mono<ServerResponse> all(ServerRequest req) {
        return ok().contentType(APPLICATION_JSON)
                .body(this.reactivePlanetRepository.findAll(), PlanetEntity.class);
    }

    public Mono<ServerResponse> findById(ServerRequest req) {
        final UUID id = UUID.fromString(req.pathVariable("id"));
        final Mono<PlanetEntity> planet = this.reactivePlanetRepository.findById(id);
        return planet
                .flatMap(p -> ok().contentType(MediaType.APPLICATION_JSON).body(fromPublisher(planet, PlanetEntity.class)))
                .switchIfEmpty(notFound().build())
                .log();
    }

    public Mono<ServerResponse> findByName(ServerRequest req) {
        String name = req.pathVariable("name");
        return ok().contentType(APPLICATION_JSON)
                .body(fromPublisher(
                        this.reactivePlanetRepository.findAllByName(name),
                        PlanetEntity.class)
                );
    }

    public Mono<ServerResponse> create(ServerRequest req) {
        return req.bodyToMono(PlanetEntity.class)
                .flatMap(planetEntity -> this.reactivePlanetRepository.save(planetEntity))
                .flatMap(p -> ServerResponse.created(URI.create(PlanetRoute.PATH + p.getId())).build());
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
        final UUID id = UUID.fromString(req.pathVariable("id"));
        return this.reactivePlanetRepository
                .findById(id)
                .flatMap(p -> noContent().build(this.reactivePlanetRepository.delete(p)))
                .switchIfEmpty(notFound().build());
    }

}
