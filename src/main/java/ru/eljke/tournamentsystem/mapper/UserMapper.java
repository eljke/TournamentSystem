package ru.eljke.tournamentsystem.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.model.Role;
import ru.eljke.tournamentsystem.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "fullname", expression = "java(user.getLastname() + \" \" + user.getFirstname() + \" \" + user.getPatronymic())")
    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "localDateToString")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "setToString")
    UserDTO userToUserDTO(User user);
    List<UserDTO> usersToUserDTOs(List<User> users);

    @Named("localDateToString")
    default String localDateToString(LocalDate date) {
        if (date == null) {
            return "";
        }

        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Named("setToString")
    static String setToString(Set<Role> roles) {
        if (roles != null) {
            return roles.stream()
                    .map(Role::name)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }
}
