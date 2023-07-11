package ru.eljke.tournamentsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.eljke.tournamentsystem.dto.ResultDTO;
import ru.eljke.tournamentsystem.entity.Result;
import ru.eljke.tournamentsystem.entity.Team;
import ru.eljke.tournamentsystem.entity.User;

@Mapper
public interface ResultMapper {
    ResultMapper INSTANCE = Mappers.getMapper(ResultMapper.class);

    ResultDTO resultToResultDTO(Result result);

    default String winnerToString(User user) {
        if (user == null) {
            return null;
        }
        return UserMapper.INSTANCE.userToUserDTO(user).getFullname();
    }

    default String winnerTeamToString(Team team) {
        if (team == null) {
            return null;
        }
        return TeamMapper.INSTANCE.teamToTeamDTO(team).getName();
    }
}
