package ru.eljke.tournamentsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.eljke.tournamentsystem.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
