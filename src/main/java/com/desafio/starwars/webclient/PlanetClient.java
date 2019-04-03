package com.desafio.starwars.webclient;

import dto.PlanetApiDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class PlanetClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanetClient.class);

    private WebClient client = WebClient.builder()
            .baseUrl("https://swapi.co/api/")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public Flux<PlanetApiDto> getApiAllPlanets() {
        return this.client
                .get().uri("/planets/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToFlux(PlanetApiDto.class);
    }

}
