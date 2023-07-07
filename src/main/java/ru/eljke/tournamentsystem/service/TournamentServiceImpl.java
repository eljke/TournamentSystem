package ru.eljke.tournamentsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.eljke.tournamentsystem.model.Tournament;
import ru.eljke.tournamentsystem.repository.TournamentRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements DatabaseService<Tournament> {
    private final TournamentRepository repository;
    @Override
    public Optional<Tournament> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Tournament> getByParam(String name) {
        return repository.findTournamentByName(name);
    }

    @Override
    public Page<Tournament> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Tournament create(Tournament tournament) {
        return repository.save(tournament);
    }

    @Override
    public Tournament update(Tournament tournament, Long id) {
        Optional<Tournament> existingTournamentOptional = repository.findById(id);
        if (existingTournamentOptional.isPresent()) {
            Tournament existingTournament = existingTournamentOptional.get();
            if (tournament.getName() != null) {
                existingTournament.setName(tournament.getName());
            }
            if (tournament.getCity() != null) {
                existingTournament.setCity(tournament.getCity());
            }
            if (tournament.getParticipantCities() != null) {
                existingTournament.setParticipantCities(tournament.getParticipantCities());
            }
            if (tournament.getMinParticipants() != null) {
                existingTournament.setMinParticipants(tournament.getMinParticipants());
            }
            if (tournament.getMaxParticipants() != null) {
                existingTournament.setMaxParticipants(tournament.getMaxParticipants());
            }
            if (tournament.getAgeLimit() != null) {
                existingTournament.setAgeLimit(tournament.getAgeLimit());
            }
            if (tournament.getAllowedClasses() != null) {
                existingTournament.setAllowedClasses(tournament.getAllowedClasses());
            }
            if (tournament.getCompetitionType() != null) {
                existingTournament.setCompetitionType(tournament.getCompetitionType());
            }
            if (tournament.getTeamSize() != null) {
                existingTournament.setTeamSize(tournament.getTeamSize());
            }
            if (tournament.getTournamentType() != null) {
                existingTournament.setTournamentType(tournament.getTournamentType());
            }

            return repository.save(existingTournament);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        }
    }
}
