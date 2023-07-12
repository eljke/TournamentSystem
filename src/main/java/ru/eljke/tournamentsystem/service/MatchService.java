package ru.eljke.tournamentsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import ru.eljke.tournamentsystem.dto.MatchDTO;
import ru.eljke.tournamentsystem.entity.Match;

public interface MatchService {
    MatchDTO getMatchById(Long matchId);
    Page<MatchDTO> getAllMatches(Pageable pageable);
    MatchDTO createMatch(Match match, Long tournamentId, Authentication auth);
    MatchDTO updateMatchById(Match match, Long matchId, Long tournamentId, Authentication auth);
    void deleteMatchById(Long matchId, Long tournamentId, Authentication auth);
    MatchDTO setMatchParticipantsAndResult(Match match, Long tournamentId, Authentication auth);
}
