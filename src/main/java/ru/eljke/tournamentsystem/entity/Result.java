package ru.eljke.tournamentsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(id, result.id) && Objects.equals(winner, result.winner) && Objects.equals(loser, result.loser) && Objects.equals(winnerTeam, result.winnerTeam) && Objects.equals(loserTeam, result.loserTeam) && Objects.equals(isDraw, result.isDraw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, winner, loser, winnerTeam, loserTeam, isDraw);
    }
}
