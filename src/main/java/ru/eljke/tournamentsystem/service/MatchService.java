package ru.eljke.tournamentsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.eljke.tournamentsystem.dto.MatchDTO;
import ru.eljke.tournamentsystem.entity.Match;

public interface MatchService {
    MatchDTO getMatchById(Long matchId);
    Page<MatchDTO> getAllMatches(Pageable pageable);
    MatchDTO createMatch(Match match, Long tournamentId);
    MatchDTO updateMatchById(Match match, Long matchId, Long tournamentId);
    void deleteMatchById(Long matchId, Long tournamentId);
    MatchDTO setMatchParticipantsAndResult(Match match, Match matchToUpdate, Long tournamentId);
}
