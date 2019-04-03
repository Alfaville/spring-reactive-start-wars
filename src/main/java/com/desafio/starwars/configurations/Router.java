package com.desafio.starwars.configurations;

import com.desafio.starwars.handles.PlanetHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Router {

    @Bean
    protected RouterFunction<ServerResponse> routes(PlanetHandler planetHandler) {
        return route(GET(RouterUtils.PlanetRoute.PATH).and(accept(APPLICATION_JSON)), planetHandler::all)
                .andRoute(GET(RouterUtils.PlanetRoute.PATH_BY_ID).and(accept(APPLICATION_JSON)), planetHandler::findById)
                .andRoute(GET(RouterUtils.PlanetRoute.PATH_BY_NAME).and(accept(APPLICATION_JSON)), planetHandler::findByName)
                .andRoute(POST(RouterUtils.PlanetRoute.PATH).and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), planetHandler::create)
                .andRoute(DELETE(RouterUtils.PlanetRoute.PATH_BY_ID), planetHandler::delete)
                .andRoute(GET(RouterUtils.StarWarsRoute.PATH), planetHandler::allPlanetApi);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RouterUtils {

        private static final String CONTEXT = "/star-wars";

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class PlanetRoute {
            public static final String PATH = CONTEXT + "/planets";
            public static final String PATH_BY_ID = CONTEXT + "/planets/{id}";
            public static final String PATH_BY_NAME = CONTEXT + "/planets/filterByName/{name}";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class StarWarsRoute {
            public static final String PATH = CONTEXT + "/swapi/api/planets";
        }
    }

}
