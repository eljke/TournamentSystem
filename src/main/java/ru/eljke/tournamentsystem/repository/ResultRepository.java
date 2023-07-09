package ru.eljke.tournamentsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.eljke.tournamentsystem.model.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
}
