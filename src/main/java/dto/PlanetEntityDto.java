package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlanetEntityDto {

    private String name;

    private String terrain;

    private String climate;

    private String rotation_period;

    private String orbital_period;

    private String diameter;

    private String gravity;

    private String surface_water;

    private String population;

    private List<String> films;

    private List<String> residents;

    private LocalDateTime created;

    private LocalDateTime edited;

    private String url;

}
