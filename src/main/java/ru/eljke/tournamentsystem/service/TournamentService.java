package ru.eljke.tournamentsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.eljke.tournamentsystem.model.Tournament;

import java.time.LocalDateTime;

public interface TournamentService {
    Tournament getTournamentById(Long id);
    Page<Tournament> getAllTournaments(Pageable pageable);
    Tournament createTournament(Tournament tournament);
    Tournament updateTournament(Tournament tournament, Long id);
    void cancelTournamentById(Long id);
    void deleteTournamentById(Long id);
    Page<Tournament> findPastTournaments(Pageable pageable);
    Page<Tournament> findCurrentTournaments(Pageable pageable);
    Page<Tournament> findUpcomingTournaments(Pageable pageable);
    Page<Tournament> findTournamentsBetweenDates(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);
    Page<Tournament> findPastTournamentsByUserId(Pageable pageable, Long userId);
    Page<Tournament> findCurrentTournamentsByUserId(Pageable pageable, Long userId);
    Page<Tournament> findUpcomingTournamentsByUserId(Pageable pageable, Long userId);
    Page<Tournament> findTournamentsBetweenDatesByUserId(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate, Long userId);
    Page<Tournament> findPastTournamentsByTeamId(Pageable pageable, Long teamId);
    Page<Tournament> findCurrentTournamentsByTeamId(Pageable pageable, Long teamId);
    Page<Tournament> findUpcomingTournamentsByTeamId(Pageable pageable, Long teamId);
    Page<Tournament> findTournamentsBetweenDatesByTeamId(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate, Long teamId);
}
