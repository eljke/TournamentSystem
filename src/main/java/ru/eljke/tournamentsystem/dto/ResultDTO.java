package ru.eljke.tournamentsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDTO {
    private String winner;
    private String loser;
    private String winnerTeam;
    private String loserTeam;
    private Boolean isDraw;
}
