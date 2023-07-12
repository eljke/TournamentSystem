package ru.eljke.tournamentsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.eljke.tournamentsystem.dto.TournamentDTO;
import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.exception.ForbiddenException;
import ru.eljke.tournamentsystem.mapper.TournamentMapper;
import ru.eljke.tournamentsystem.entity.*;
import ru.eljke.tournamentsystem.mapper.UserMapper;
import ru.eljke.tournamentsystem.repository.TeamRepository;
import ru.eljke.tournamentsystem.repository.TournamentRepository;
import ru.eljke.tournamentsystem.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository repository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Override
    public TournamentDTO getTournamentById(Long id) {
        return TournamentMapper.INSTANCE.tournamentToTournamentDTO(repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found")));
    }

    @Override
    public Page<TournamentDTO> getAllTournaments(Pageable pageable) {
        return repository.findAll(pageable).map(TournamentMapper.INSTANCE::tournamentToTournamentDTO);
    }

    @Override
    public TournamentDTO createTournament(Tournament tournament, Authentication auth) {
        UserDTO userDTO = getUserFromAuthentication(auth);

        validateOrganizingSchool(tournament, userDTO);

        return TournamentMapper.INSTANCE.tournamentToTournamentDTO(repository.save(tournament));
    }

    @Override
    public TournamentDTO updateTournament(Tournament tournament, Long id, Authentication auth) {
        UserDTO userDTO = getUserFromAuthentication(auth);

        Tournament existingTournament = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        validateOrganizingSchool(existingTournament, userDTO);

        if (existingTournament.getStage() == TournamentStage.REGISTRATION) {
            if (tournament.getName() != null) {
                existingTournament.setName(tournament.getName());
            }
            if (tournament.getCity() != null) {
                existingTournament.setCity(tournament.getCity());
            }
            if (tournament.getOrganizingSchool() != null) {
                existingTournament.setOrganizingSchool(tournament.getOrganizingSchool());
            }
            if (tournament.getStartDateTime() != null) {
                existingTournament.setStartDateTime(tournament.getStartDateTime());
            }
            if (tournament.getEndDate() != null) {
                existingTournament.setEndDate(tournament.getEndDate());
            }
            if (tournament.getParticipatingCities() != null) {
                existingTournament.setParticipatingCities(tournament.getParticipatingCities());
            }
            if (tournament.getSoloParticipants() != null) {
                Set<User> soloParticipants = new HashSet<>();
                for (User soloParticipant : tournament.getSoloParticipants()) {
                    soloParticipants.add(userRepository.findById(soloParticipant.getId())
                            .orElseThrow(() -> new IllegalArgumentException("No user found with id = " + soloParticipant.getId())));
                }
                existingTournament.setSoloParticipants(soloParticipants);
            }
            if (tournament.getTeamParticipants() != null) {
                Set<Team> teamParticipants = new HashSet<>();
                for (Team teamParticipant : tournament.getTeamParticipants()) {
                    teamParticipants.add(teamRepository.findById(teamParticipant.getId())
                            .orElseThrow(() -> new IllegalArgumentException("No user found with id = " + teamParticipant.getId())));
                }
                existingTournament.setTeamParticipants(teamParticipants);
            }
            if (tournament.getMinParticipants() != null) {
                existingTournament.setMinParticipants(tournament.getMinParticipants());
            }
            if (tournament.getMaxParticipants() != null) {
                existingTournament.setMaxParticipants(tournament.getMaxParticipants());
            }
            if (tournament.getMinAgeToParticipate() != null) {
                existingTournament.setMinAgeToParticipate(tournament.getMinAgeToParticipate());
            }
            if (tournament.getMaxAgeToParticipate() != null) {
                existingTournament.setMaxAgeToParticipate(tournament.getMaxAgeToParticipate());
            }
            if (tournament.getAllowedGrades() != null) {
                existingTournament.setAllowedGrades(tournament.getAllowedGrades());
            }
            if (tournament.getStage() != null) {
                existingTournament.setStage(tournament.getStage());
            }
            if (tournament.getSubject() != null) {
                existingTournament.setSubject(tournament.getSubject());
            }
            if (tournament.getType() != null) {
                existingTournament.setType(tournament.getType());
            }
            if (tournament.getTeamSize() != null) {
                existingTournament.setTeamSize(tournament.getTeamSize());
            }
            if (tournament.getIsPublic() != null) {
                existingTournament.setIsPublic(tournament.getIsPublic());
            }
            // В данном случае, поле results не изменяется, так как оно обрабатывается отдельно при добавлении результатов.
            // TODO: МОЖНО ДОБАВИТЬ ЛОГИКУ РЕДАКТИРОВАНИЯ ДЛЯ ДРУГИХ СТАДИЙ ТУРНИРА
            return TournamentMapper.INSTANCE.tournamentToTournamentDTO(repository.save(existingTournament));
        } else {
            throw new UnsupportedOperationException("Tournament cannot be edited at the current stage");
        }
    }

    @Override
    public void cancelTournamentById(Long id, Authentication auth) {
        UserDTO userDTO = getUserFromAuthentication(auth);

        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        validateOrganizingSchool(tournament, userDTO);

        if (tournament.getStage() == TournamentStage.REGISTRATION) {
            repository.cancelTournamentById(id);
        } else if (tournament.getStage() == TournamentStage.CANCELED) {
            throw new UnsupportedOperationException("Tournament with id = " + id + " is already cancelled");
        } else {
            throw new UnsupportedOperationException("Tournament cannot be canceled at the current stage");
        }
    }

    @Override
    public void deleteTournamentById(Long id, Authentication auth) {
        UserDTO userDTO = getUserFromAuthentication(auth);

        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        validateOrganizingSchool(tournament, userDTO);

        repository.deleteById(id);
    }

    @Override
    public Page<TournamentDTO> findPastTournaments(Pageable pageable) {
        return repository.findPastTournaments(pageable).map(TournamentMapper.INSTANCE::tournamentToTournamentDTO);
    }

    @Override
    public Page<TournamentDTO> findCurrentTournaments(Pageable pageable) {
        return repository.findCurrentTournaments(pageable).map(TournamentMapper.INSTANCE::tournamentToTournamentDTO);
    }

    @Override
    public Page<TournamentDTO> findUpcomingTournaments(Pageable pageable) {
        return repository.findUpcomingTournaments(pageable).map(TournamentMapper.INSTANCE::tournamentToTournamentDTO);
    }

    @Override
    public Page<TournamentDTO> findTournamentsBetweenDates(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findTournamentsBetweenDates(pageable, startDate, endDate).map(TournamentMapper.INSTANCE::tournamentToTournamentDTO);
    }

    @Override
    public Page<TournamentDTO> findPastTournamentsByUserId(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return repository.findPastTournamentsByUser(pageable, user).map(TournamentMapper.INSTANCE::tournamentToTournamentDTO);
    }

    @Override
    public Page<TournamentDTO> findCurrentTournamentsByUserId(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return repository.findCurrentTournamentsByUser(pageable, user).map(TournamentMapper.INSTANCE::tournamentToTournamentDTO);
    }

    @Override
    public Page<TournamentDTO> findUpcomingTournamentsByUserId(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return repository.findUpcomingTournamentsByUser(pageable, user).map(TournamentMapper.INSTANCE::tournamentToTournamentDTO);
    }

    @Override
    public Page<TournamentDTO> findTournamentsBetweenDatesByUserId(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return repository.findTournamentsBetweenDatesByUser(pageable, startDate, endDate, user).map(TournamentMapper.INSTANCE::tournamentToTournamentDTO);
    }

    @Override
    public Page<TournamentDTO> findPastTournamentsByTeamId(Pageable pageable, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        return repository.findPastTournamentsByTeam(pageable, team).map(TournamentMapper.INSTANCE::tournamentToTournamentDTO);
    }

    @Override
    public Page<TournamentDTO> findCurrentTournamentsByTeamId(Pageable pageable, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        return repository.findCurrentTournamentsByTeam(pageable, team).map(TournamentMapper.INSTANCE::tournamentToTournamentDTO);
    }

    @Override
    public Page<TournamentDTO> findUpcomingTournamentsByTeamId(Pageable pageable, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        return repository.findUpcomingTournamentsByTeam(pageable, team).map(TournamentMapper.INSTANCE::tournamentToTournamentDTO);
    }

    @Override
    public Page<TournamentDTO> findTournamentsBetweenDatesByTeamId(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        return repository.findTournamentsBetweenDatesByTeam(pageable, startDate, endDate, team).map(TournamentMapper.INSTANCE::tournamentToTournamentDTO);
    }

    @Override
    public UserDTO getUserFromAuthentication(Authentication auth) {
        if (auth != null && auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ADMIN"))
        ){
            UserDTO userDTO = new UserDTO();
            userDTO.setRoles(auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", ")));
            return userDTO;
        } else if (auth != null && auth.getAuthorities() != null) {
            User userFromDB = userRepository.findUserByUsername(auth.getName());
            if (userFromDB != null) {
                return UserMapper.INSTANCE.userToUserDTO(userFromDB);
            }
        }

        throw new ForbiddenException("Forbidden: user access not resolved");
    }

    private void validateOrganizingSchool(Tournament tournament, UserDTO userDTO) {
        if (!userDTO.getRoles().contains("ADMIN")) {
            String teacherSchools = userDTO.getSchool();
            if (tournament.getOrganizingSchool() == null || !teacherSchools.contains(tournament.getOrganizingSchool())) {
                throw new ForbiddenException("Teacher's school does not match tournament's organizing school");
            }
        }
    }
}
