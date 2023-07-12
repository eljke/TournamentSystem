package ru.eljke.tournamentsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(name = "Result Data Transfer Object")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDTO {
    @Schema(name = "Match solo winner")
    private String winner;
    @Schema(name = "Match solo loser")
    private String loser;
    @Schema(name = "Match team winner")
    private String winnerTeam;
    @Schema(name = "Match team loser")
    private String loserTeam;
    @Schema(name = "Have match ended in a draw?")
    private Boolean isDraw;
}
