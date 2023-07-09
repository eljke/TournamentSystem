package ru.eljke.tournamentsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.eljke.tournamentsystem.dto.TournamentDTO;
import ru.eljke.tournamentsystem.model.Tournament;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper
public interface TournamentMapper {
    TournamentMapper INSTANCE = Mappers.getMapper(TournamentMapper.class);

    @Mapping(source = "startDateTime", target = "startDateTime", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "endDate", target = "endDate", qualifiedByName = "localDateToString")
    TournamentDTO tournamentToTournamentDTO(Tournament user);
    List<TournamentDTO> tournamentsToTournamentDTOs(List<Tournament> users);

    @Named("localDateToString")
    default String localDateToString(LocalDate date) {
        if (date == null) {
            return "";
        }

        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime date) {
        if (date == null) {
            return "";
        }

        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
