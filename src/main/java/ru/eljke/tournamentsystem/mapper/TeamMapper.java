package ru.eljke.tournamentsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.eljke.tournamentsystem.dto.TeamDTO;
import ru.eljke.tournamentsystem.entity.Team;
import ru.eljke.tournamentsystem.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    @Mapping(target = "members", qualifiedByName = "listToString")
    TeamDTO teamToTeamDTO(Team team);

    @Named("listToString")
    static String listToString(List<User> members) {
        if (members != null) {
            return members.stream()
                    .map(User::getFullName)
                    .collect(Collectors.joining(", "));
        }
        return null;
    }
}
