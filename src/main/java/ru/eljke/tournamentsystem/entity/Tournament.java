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
    @Schema(name = "Autogenerated ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Schema(name = "Tournament name")
    private String name;
    @Schema(name = "Tournament city")
    private String city;
    @Schema(name = "Tournament organizing school")
    private String organizingSchool;

    @Schema(name = "Solo participants")
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @ToString.Exclude
    private Set<User> soloParticipants;
    @Schema(name = "Team participants")
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @ToString.Exclude
    private Set<Team> teamParticipants;

    @Schema(name = "Start date and time", pattern = "dd/MM/yyyy HH:mm")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime startDateTime;
    @Schema(name = "End date", pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;
    @Schema(name = "Tournament participating cities")
    @ElementCollection
    private Set<String> participatingCities;
    @Schema(name = "Tournament min participants")
    private Integer minParticipants;
    @Schema(name = "Tournament max participants")
    private Integer maxParticipants;
    @Schema(name = "Min age to participate in the tournament")
    private Integer minAgeToParticipate;
    @Schema(name = "Max age to participate in the tournament")
    private Integer maxAgeToParticipate;
    @Schema(name = "Tournament allowed grades")
    @ElementCollection
    private Set<String> allowedGrades;
    @Schema(name = "Tournament stage")
    @Enumerated(EnumType.STRING)
    private TournamentStage stage;
    @Schema(name = "Tournament subject")
    @Enumerated(EnumType.STRING)
    private TournamentSubject subject;
    @Schema(name = "Tournament type")
    @Enumerated(EnumType.STRING)
    private TournamentType type;
    @Schema(name = "Team size")
    private Integer teamSize;
    @Schema(name = "Is tournament public")
    private Boolean isPublic;
    @Schema(name = "Tournament matches")
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
