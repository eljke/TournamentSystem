package ru.eljke.tournamentsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(name = "Team Data Transfer Object")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamDTO {
    @Schema(name = "Autogenerated ID")
    private Long id;
    @Schema(name = "Team name")
    private String name;
    @Schema(name = "Team members")
    private String members;
}
