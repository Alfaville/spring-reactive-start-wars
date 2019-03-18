package com.desafio.starwars.entity;


import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@Table("planets")
public class PlanetEntity {

    @PrimaryKey("planet_id")
    private UUID id = UUID.randomUUID();

    private String name;

    private String terrain;

    private String climate;
}