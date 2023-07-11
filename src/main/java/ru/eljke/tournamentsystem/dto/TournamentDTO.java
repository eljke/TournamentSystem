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
public class TournamentDTO {
    private Long id;
    private String name;
    private String city;
    private String organizingSchool;
    private String startDateTime;
    private String endDate;
    private Integer minParticipants;
    private Integer maxParticipants;
    private Integer minAgeToParticipate;
    private Integer maxAgeToParticipate;
    private String stage;
    private String subject;
    private String type;
    private Integer teamSize;
    private Boolean isPublic;
}
