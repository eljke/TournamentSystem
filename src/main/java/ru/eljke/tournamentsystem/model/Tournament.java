package ru.eljke.tournamentsystem.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Schema(description = "Member details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String city;

    @ElementCollection
    private List<String> participantCities;

    private Integer minParticipants;
    private Integer maxParticipants;
    private Integer ageLimit;

    @ElementCollection
    private List<String> allowedClasses;

    private String competitionType;
    private Integer teamSize;

    private String tournamentType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tournament that = (Tournament) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(city, that.city) && Objects.equals(participantCities, that.participantCities) && Objects.equals(minParticipants, that.minParticipants) && Objects.equals(maxParticipants, that.maxParticipants) && Objects.equals(ageLimit, that.ageLimit) && Objects.equals(allowedClasses, that.allowedClasses) && Objects.equals(competitionType, that.competitionType) && Objects.equals(teamSize, that.teamSize) && Objects.equals(tournamentType, that.tournamentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city, participantCities, minParticipants, maxParticipants, ageLimit, allowedClasses, competitionType, teamSize, tournamentType);
    }
}
