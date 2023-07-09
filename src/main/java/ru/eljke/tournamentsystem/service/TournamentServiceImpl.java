package ru.eljke.tournamentsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.eljke.tournamentsystem.model.*;
import ru.eljke.tournamentsystem.repository.TeamRepository;
import ru.eljke.tournamentsystem.repository.TournamentRepository;
import ru.eljke.tournamentsystem.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository repository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Override
    public Tournament getTournamentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
    }

    @Override
    public Page<Tournament> getAllTournaments(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Tournament createTournament(Tournament tournament) {
        return repository.save(tournament);
    }

    @Override
    public Tournament updateTournament(Tournament tournament, Long id) {
        Tournament existingTournament = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

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
            return repository.save(existingTournament);
        } else {
            throw new UnsupportedOperationException("Tournament cannot be edited at the current stage");
        }
    }

    @Override
    public void cancelTournamentById(Long id) {
        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        if (tournament.getStage() == TournamentStage.REGISTRATION) {
            repository.cancelTournamentById(id);
        } else {
            throw new UnsupportedOperationException("Tournament cannot be canceled at the current stage");
        }
    }

    @Override
    public void deleteTournamentById(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        repository.deleteById(id);
    }

    @Override
    public Page<Tournament> findPastTournaments(Pageable pageable) {
        return repository.findPastTournaments(pageable);
    }

    @Override
    public Page<Tournament> findCurrentTournaments(Pageable pageable) {
        return repository.findCurrentTournaments(pageable);
    }

    @Override
    public Page<Tournament> findUpcomingTournaments(Pageable pageable) {
        return repository.findUpcomingTournaments(pageable);
    }

    @Override
    public Page<Tournament> findTournamentsBetweenDates(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findTournamentsBetweenDates(pageable, startDate, endDate);
    }

    @Override
    public Page<Tournament> findPastTournamentsByUserId(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return repository.findPastTournamentsByUser(pageable, user);
    }

    @Override
    public Page<Tournament> findCurrentTournamentsByUserId(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return repository.findCurrentTournamentsByUser(pageable, user);
    }

    @Override
    public Page<Tournament> findUpcomingTournamentsByUserId(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return repository.findUpcomingTournamentsByUser(pageable, user);
    }

    @Override
    public Page<Tournament> findTournamentsBetweenDatesByUserId(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return repository.findTournamentsBetweenDatesByUser(pageable, startDate, endDate, user);
    }

    @Override
    public Page<Tournament> findPastTournamentsByTeamId(Pageable pageable, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        return repository.findPastTournamentsByTeam(pageable, team);
    }

    @Override
    public Page<Tournament> findCurrentTournamentsByTeamId(Pageable pageable, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        return repository.findCurrentTournamentsByTeam(pageable, team);
    }

    @Override
    public Page<Tournament> findUpcomingTournamentsByTeamId(Pageable pageable, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        return repository.findUpcomingTournamentsByTeam(pageable, team);
    }

    @Override
    public Page<Tournament> findTournamentsBetweenDatesByTeamId(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        return repository.findTournamentsBetweenDatesByTeam(pageable, startDate, endDate, team);
    }
}
