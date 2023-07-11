package ru.eljke.tournamentsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Schema(description = "Match between 2 participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateTime;

    @JsonProperty("team1")
    @ManyToOne
    private Team teamParticipant1;
    @JsonProperty("team2")
    @ManyToOne
    private Team teamParticipant2;

    @JsonProperty("solo1")
    @ManyToOne
    private User soloParticipant1;
    @JsonProperty("solo2")
    @ManyToOne
    private User soloParticipant2;

    @OneToOne(targetEntity = Result.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "result_id")
    private Result result;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(id, match.id) && Objects.equals(dateTime, match.dateTime) && Objects.equals(teamParticipant1, match.teamParticipant1) && Objects.equals(teamParticipant2, match.teamParticipant2) && Objects.equals(soloParticipant1, match.soloParticipant1) && Objects.equals(soloParticipant2, match.soloParticipant2) && Objects.equals(result, match.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, teamParticipant1, teamParticipant2, soloParticipant1, soloParticipant2, result);
    }
}
