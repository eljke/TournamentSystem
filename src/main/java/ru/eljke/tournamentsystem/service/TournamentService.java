package ru.eljke.tournamentsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import ru.eljke.tournamentsystem.dto.TournamentDTO;
import ru.eljke.tournamentsystem.entity.Tournament;

import java.time.LocalDateTime;

public interface TournamentService {
    TournamentDTO getTournamentById(Long id);
    Page<TournamentDTO> getAllTournaments(Pageable pageable);
    TournamentDTO createTournament(Tournament tournament, Authentication auth);
    TournamentDTO updateTournament(Tournament tournament, Long id, Authentication auth);
    void cancelTournamentById(Long id, Authentication auth);
    void deleteTournamentById(Long id, Authentication auth);
    Page<TournamentDTO> findPastTournaments(Pageable pageable);
    Page<TournamentDTO> findCurrentTournaments(Pageable pageable);
    Page<TournamentDTO> findUpcomingTournaments(Pageable pageable);
    Page<TournamentDTO> findTournamentsBetweenDates(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);
    Page<TournamentDTO> findPastTournamentsByUserId(Pageable pageable, Long userId);
    Page<TournamentDTO> findCurrentTournamentsByUserId(Pageable pageable, Long userId);
    Page<TournamentDTO> findUpcomingTournamentsByUserId(Pageable pageable, Long userId);
    Page<TournamentDTO> findTournamentsBetweenDatesByUserId(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate, Long userId);
    Page<TournamentDTO> findPastTournamentsByTeamId(Pageable pageable, Long teamId);
    Page<TournamentDTO> findCurrentTournamentsByTeamId(Pageable pageable, Long teamId);
    Page<TournamentDTO> findUpcomingTournamentsByTeamId(Pageable pageable, Long teamId);
    Page<TournamentDTO> findTournamentsBetweenDatesByTeamId(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate, Long teamId);
}
