package ru.eljke.tournamentsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Schema(description = "Match result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    private User winner;
    @ManyToOne
    private User loser;
    @ManyToOne
    private Team winnerTeam;
    @ManyToOne
    private Team loserTeam;
    private Boolean isDraw;
}
