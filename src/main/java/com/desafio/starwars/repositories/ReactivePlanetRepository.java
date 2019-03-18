package com.desafio.starwars.repositories;

import com.desafio.starwars.entity.PlanetEntity;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface ReactivePlanetRepository extends ReactiveCassandraRepository<PlanetEntity, UUID> {

    @AllowFiltering
    Flux<PlanetEntity> findAllByName(final String name);

}
