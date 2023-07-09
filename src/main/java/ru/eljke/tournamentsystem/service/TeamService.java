package ru.eljke.tournamentsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.eljke.tournamentsystem.model.Team;

public interface TeamService {
    Team getTeamById(Long teamId);
    Page<Team> getAllTeams(Pageable pageable);
    Team createTeam(Team team);
    Team updateTeamById(Team team, Long teamId);
    void deleteTeamById(Long teamId);
}
