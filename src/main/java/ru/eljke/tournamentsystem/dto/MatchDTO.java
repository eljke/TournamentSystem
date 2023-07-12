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
public class MatchDTO {
    private Long id;
    private String dateTime;
    private String participant1;
    private String participant2;
    private String team1;
    private String team2;
    private ResultDTO result;
}
