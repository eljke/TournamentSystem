package ru.eljke.tournamentsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.eljke.tournamentsystem.model.Match;

public interface MatchService {
    Match getMatchById(Long matchId);
    Page<Match> getAllMatches(Pageable pageable);
    Match createMatch(Match match);
    Match updateMatchById(Match match, Long matchId);
    void deleteMatchById(Long matchId);
}
