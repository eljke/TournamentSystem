package ru.eljke.tournamentsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.eljke.tournamentsystem.model.Match;
import ru.eljke.tournamentsystem.repository.*;


@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final MatchRepository repository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ResultRepository resultRepository;

    @Override
    public Match getMatchById(Long matchId) {
        return repository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));
    }

    @Override
    public Page<Match> getAllMatches(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Match createMatch(Match match) {
        return setMatchParticipantsAndResult(match, match);
    }

    @Override
    public Match updateMatchById(Match match, Long matchId) {
        Match matchToUpdate = repository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        if (match.getDateTime() != null) {
            matchToUpdate.setDateTime(match.getDateTime());
        }
        return setMatchParticipantsAndResult(match, matchToUpdate);
    }

    @Override
    public void deleteMatchById(Long matchId) {
        repository.deleteById(matchId);
    }

    private Match setMatchParticipantsAndResult(Match match, Match matchToUpdate) {
        // TODO: ДОП. ЛОГИКА ОБРАБОТКИ
        if (match.getSoloParticipant1() != null) {
            matchToUpdate.setSoloParticipant1(userRepository.findById(match.getSoloParticipant1().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found")));
        }
        if (match.getSoloParticipant2() != null) {
            matchToUpdate.setSoloParticipant2(userRepository.findById(match.getSoloParticipant2().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found")));
        }
        if (match.getTeamParticipant1() != null) {
            matchToUpdate.setTeamParticipant1(teamRepository.findById(match.getTeamParticipant1().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Team not found")));
        }
        if (match.getTeamParticipant2() != null) {
            matchToUpdate.setTeamParticipant2(teamRepository.findById(match.getTeamParticipant2().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Team not found")));
        }
        if (match.getResult() != null) {
            matchToUpdate.setResult(resultRepository.save(match.getResult()));
        }

        return repository.save(matchToUpdate);
    }
}
