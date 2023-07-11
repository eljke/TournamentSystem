package ru.eljke.tournamentsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Schema(description = "Tournament")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String city;
    private String organizingSchool;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @ToString.Exclude
    Set<User> soloParticipants;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @ToString.Exclude
    Set<Team> teamParticipants;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime startDateTime;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;
    @ElementCollection
    private Set<String> participatingCities;
    private Integer minParticipants;
    private Integer maxParticipants;
    private Integer minAgeToParticipate;
    private Integer maxAgeToParticipate;
    @ElementCollection
    private Set<String> allowedGrades;
    @Enumerated(EnumType.STRING)
    private TournamentStage stage;
    @Enumerated(EnumType.STRING)
    private TournamentSubject subject;
    @Enumerated(EnumType.STRING)
    private TournamentType type;
    private Integer teamSize;
    private Boolean isPublic;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "tournament_id")
    @ToString.Exclude
    private List<Match> matches;

    public void setSubject(TournamentSubject subject) {
        this.subject = subject;
        if (subject == TournamentSubject.CHESS) {
            this.teamSize = 1; // Устанавливаем размер команды равным 1 для шахмат
        }
    }

    public void setTeamSize(Integer teamSize) {
        if (subject != TournamentSubject.CHESS) {
            this.teamSize = teamSize;
        }
    }

    public void addMatch(Match match) {
        this.matches.add(match);
    }

    public void removeMatch(Match match) {
        this.matches.remove(match);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tournament that = (Tournament) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(city, that.city) && Objects.equals(organizingSchool, that.organizingSchool) && Objects.equals(soloParticipants, that.soloParticipants) && Objects.equals(teamParticipants, that.teamParticipants) && Objects.equals(startDateTime, that.startDateTime) && Objects.equals(endDate, that.endDate) && Objects.equals(participatingCities, that.participatingCities) && Objects.equals(minParticipants, that.minParticipants) && Objects.equals(maxParticipants, that.maxParticipants) && Objects.equals(minAgeToParticipate, that.minAgeToParticipate) && Objects.equals(maxAgeToParticipate, that.maxAgeToParticipate) && Objects.equals(allowedGrades, that.allowedGrades) && stage == that.stage && subject == that.subject && type == that.type && Objects.equals(teamSize, that.teamSize) && Objects.equals(isPublic, that.isPublic) && Objects.equals(matches, that.matches);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city, organizingSchool, soloParticipants, teamParticipants, startDateTime, endDate, participatingCities, minParticipants, maxParticipants, minAgeToParticipate, maxAgeToParticipate, allowedGrades, stage, subject, type, teamSize, isPublic, matches);
    }
}
