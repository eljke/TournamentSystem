package ru.eljke.tournamentsystem.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.entity.Role;
import ru.eljke.tournamentsystem.entity.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "fullname", expression = "java(user.getFullName())")
    @Mapping(target = "birthDate", expression = "java(localDateToString(user.getBirthDate()))")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "setToString")
    UserDTO userToUserDTO(User user);
    List<UserDTO> usersToUserDTOs(List<User> users);

    default String localDateToString(LocalDate date) {
        if (date == null) {
            return null;
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
        return null;
    }
}
