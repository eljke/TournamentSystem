package ru.eljke.tournamentsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.eljke.tournamentsystem.model.Team;
import ru.eljke.tournamentsystem.model.User;
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
    public Team getTeamById(Long teamId) {
        return repository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
    }

    @Override
    public Page<Team> getAllTeams(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Team createTeam(Team team) {
        List<User> members = new ArrayList<>();
        // TODO: РЕАЛИЗОВАТЬ ДОБАВЛЕНИЕ ВСЕХ ЮЗЕРОВ, А НЕ ТОЛЬКО 1
        members.add(userRepository.findById(team.getMembers().get(0).getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found")));

        team.setMembers(members);
        return repository.save(team);
    }

    @Override
    public Team updateTeamById(Team team, Long teamId) {
        Team teamToUpdate = repository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        if (team.getName() != null) {
            teamToUpdate.setName(team.getName());
        }
        if (team.getMembers() != null) {
            teamToUpdate.setMembers(team.getMembers());
        }

        return repository.save(teamToUpdate);
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
