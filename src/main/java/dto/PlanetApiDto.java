package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlanetApiDto {

    private int count;

    private String next;

    private List<PlanetEntityDto> results;

}
