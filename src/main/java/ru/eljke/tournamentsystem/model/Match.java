package ru.eljke.tournamentsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
}
