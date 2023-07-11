package ru.eljke.tournamentsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.eljke.tournamentsystem.dto.TeamDTO;
import ru.eljke.tournamentsystem.entity.Team;

public interface TeamService {
    TeamDTO getTeamById(Long teamId);
    Page<TeamDTO> getAllTeams(Pageable pageable);
    TeamDTO createTeam(Team team);
    TeamDTO updateTeamById(Team team, Long teamId);
    void deleteTeamById(Long teamId);
}
