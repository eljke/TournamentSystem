package ru.eljke.tournamentsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.eljke.tournamentsystem.dto.MatchDTO;
import ru.eljke.tournamentsystem.entity.Match;
import ru.eljke.tournamentsystem.entity.Tournament;
import ru.eljke.tournamentsystem.mapper.MatchMapper;
import ru.eljke.tournamentsystem.repository.*;


@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final MatchRepository repository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ResultRepository resultRepository;
    private final TournamentRepository tournamentRepository;

    @Override
    public MatchDTO getMatchById(Long matchId) {
        return MatchMapper.INSTANCE.matchToMatchDTO(repository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found")));
    }

    @Override
    public Page<MatchDTO> getAllMatches(Pageable pageable) {
        return repository.findAll(pageable).map(MatchMapper.INSTANCE::matchToMatchDTO);
    }

    @Override
    public MatchDTO createMatch(Match match, Long tournamentId) {
        return setMatchParticipantsAndResult(match, match, tournamentId);
    }

    @Override
    public MatchDTO updateMatchById(Match match, Long matchId, Long tournamentId) {
        Match matchToUpdate = repository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        if (match.getDateTime() != null) {
            matchToUpdate.setDateTime(match.getDateTime());
        }
        return setMatchParticipantsAndResult(match, matchToUpdate, tournamentId);
    }

    @Override
    public void deleteMatchById(Long matchId, Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException(""));
        tournament.removeMatch(repository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("No tournament found")));

        repository.deleteById(matchId);
    }

    @Override
    public MatchDTO setMatchParticipantsAndResult(Match match, Match matchToUpdate, Long tournamentId) {
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

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("No tournament found"));
        tournament.addMatch(match);

        return MatchMapper.INSTANCE.matchToMatchDTO(repository.save(matchToUpdate));
    }
}
