package ru.eljke.tournamentsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.eljke.tournamentsystem.dto.TeamDTO;
import ru.eljke.tournamentsystem.mapper.TeamMapper;
import ru.eljke.tournamentsystem.entity.Team;
import ru.eljke.tournamentsystem.entity.User;
import ru.eljke.tournamentsystem.repository.TeamRepository;
import ru.eljke.tournamentsystem.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository repository;
    private final UserRepository userRepository;

    @Override
    public TeamDTO getTeamById(Long teamId) {
        return TeamMapper.INSTANCE.teamToTeamDTO(repository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found")));
    }

    @Override
    public Page<TeamDTO> getAllTeams(Pageable pageable) {
        return repository.findAll(pageable).map(TeamMapper.INSTANCE::teamToTeamDTO);
    }

    @Override
    public TeamDTO createTeam(Team team) {
        List<User> members = new ArrayList<>();
        // TODO: РЕАЛИЗОВАТЬ ДОБАВЛЕНИЕ ВСЕХ ЮЗЕРОВ, А НЕ ТОЛЬКО 1
        members.add(userRepository.findById(team.getMembers().get(0).getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found")));

        team.setMembers(members);
        return TeamMapper.INSTANCE.teamToTeamDTO(repository.save(team));
    }

    @Override
    public TeamDTO updateTeamById(Team team, Long teamId) {
        Team teamToUpdate = repository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        if (team.getName() != null) {
            teamToUpdate.setName(team.getName());
        }
        if (team.getMembers() != null) {
            teamToUpdate.setMembers(team.getMembers());
        }

        return TeamMapper.INSTANCE.teamToTeamDTO(repository.save(teamToUpdate));
    }

    @Override
    public void deleteTeamById(Long teamId) {
        if (repository.findById(teamId).isPresent()) {
            repository.deleteById(teamId);
        } else {
            throw new IllegalArgumentException("Team not found");
        }
    }
}
