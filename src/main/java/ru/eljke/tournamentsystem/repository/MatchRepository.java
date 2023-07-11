package ru.eljke.tournamentsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.eljke.tournamentsystem.entity.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}
