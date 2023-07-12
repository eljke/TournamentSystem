package ru.eljke.tournamentsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.eljke.tournamentsystem.dto.MatchDTO;
import ru.eljke.tournamentsystem.dto.ResultDTO;
import ru.eljke.tournamentsystem.entity.Match;
import ru.eljke.tournamentsystem.entity.Result;
import ru.eljke.tournamentsystem.entity.Team;
import ru.eljke.tournamentsystem.entity.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mapping(target = "dateTime", expression = "java(localDateToString(match.getDateTime()))")
    @Mapping(target = "participant1", source = "soloParticipant1")
    @Mapping(target = "participant2", source = "soloParticipant2")
    @Mapping(target = "team1", source = "teamParticipant1")
    @Mapping(target = "team2", source = "teamParticipant2")
    @Mapping(target = "result", source = "result")
    MatchDTO matchToMatchDTO(Match match);

    default String localDateToString(LocalDateTime date) {
        if (date == null) {
            return null;
        }

        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    default String userToString(User user) {
        if (user == null) {
            return null;
        }
        return UserMapper.INSTANCE.userToUserDTO(user).getFullname();
    }

    default String teamToString(Team team) {
        if (team == null) {
            return null;
        }
        return TeamMapper.INSTANCE.teamToTeamDTO(team).getName();
    }

    default ResultDTO resultToResultDTO(Result result) {
        if (result == null) {
            return null;
        }

        return ResultMapper.INSTANCE.resultToResultDTO(result);
    }

    default String resultToString(Result result) {
        if (result == null) {
            return null;
        }

        return result.toString();
    }
}
