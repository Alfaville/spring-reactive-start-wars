package com.desafio.starwars.domains;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planet_id_seq")
    @SequenceGenerator(name="planet_id_seq", sequenceName = "PLANET_ID_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String terrain;

    @Version
    private Integer version;
}