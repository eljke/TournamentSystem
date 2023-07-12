package ru.eljke.tournamentsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.eljke.tournamentsystem.dto.MatchDTO;
import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.entity.Match;
import ru.eljke.tournamentsystem.entity.Tournament;
import ru.eljke.tournamentsystem.exception.ForbiddenException;
import ru.eljke.tournamentsystem.mapper.MatchMapper;
import ru.eljke.tournamentsystem.repository.*;

import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final MatchRepository repository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ResultRepository resultRepository;
    private final TournamentRepository tournamentRepository;
    private final TournamentService tournamentService;

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
    public MatchDTO createMatch(Match match, Long tournamentId, Authentication auth) {
        return setMatchParticipantsAndResult(match, tournamentId, auth);
    }

    @Override
    public MatchDTO updateMatchById(Match match, Long matchId, Long tournamentId, Authentication auth) {
        Match matchToUpdate = repository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        if (match.getDateTime() != null) {
            matchToUpdate.setDateTime(match.getDateTime());
        }
        return setMatchParticipantsAndResult(matchToUpdate, tournamentId, auth);
    }

    @Override
    public void deleteMatchById(Long matchId, Long tournamentId, Authentication auth) {
        UserDTO userDTO = tournamentService.getUserFromAuthentication(auth);
        validateOrganizingSchool(tournamentId, userDTO);

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException(""));
        tournament.removeMatch(repository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("No tournament found")));

        repository.deleteById(matchId);
    }

    @Override
    public MatchDTO setMatchParticipantsAndResult(Match match, Long tournamentId, Authentication auth) {
        System.out.println(tournamentId);
        // TODO: ДОП. ЛОГИКА ОБРАБОТКИ
        UserDTO userDTO = tournamentService.getUserFromAuthentication(auth);
        validateOrganizingSchool(tournamentId, userDTO);

        if (match.getSoloParticipant1() != null) {
            match.setSoloParticipant1(userRepository.findById(match.getSoloParticipant1().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found")));
        }
        if (match.getSoloParticipant2() != null) {
            match.setSoloParticipant2(userRepository.findById(match.getSoloParticipant2().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found")));
        }
        if (match.getTeamParticipant1() != null) {
            match.setTeamParticipant1(teamRepository.findById(match.getTeamParticipant1().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Team not found")));
        }
        if (match.getTeamParticipant2() != null) {
            match.setTeamParticipant2(teamRepository.findById(match.getTeamParticipant2().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Team not found")));
        }
        if (match.getResult() != null) {
            match.setResult(resultRepository.save(match.getResult()));
        }

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("No tournament found"));

        tournament.setMatches(new ArrayList<>());
        tournament.addMatch(match);

        return MatchMapper.INSTANCE.matchToMatchDTO(repository.save(match));
    }


    private void validateOrganizingSchool(Long tournamentId, UserDTO userDTO) {
        if (!userDTO.getRoles().contains("ADMIN")) {
            String teacherSchools = userDTO.getSchool();
            Tournament tournament = tournamentRepository.findById(tournamentId)
                    .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
            if (tournament.getOrganizingSchool() == null || !teacherSchools.contains(tournament.getOrganizingSchool())) {
                throw new ForbiddenException("Teacher's school does not match tournament's organizing school");
            }
        }
    }
}
