package ru.eljke.tournamentsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.eljke.tournamentsystem.entity.Team;
import ru.eljke.tournamentsystem.entity.Tournament;
import ru.eljke.tournamentsystem.entity.User;

import java.time.LocalDateTime;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Tournament t SET t.stage = 'CANCELED' WHERE t.id = :id")
    void cancelTournamentById(@Param("id") Long id);
    @Query("SELECT t FROM Tournament t WHERE t.endDate < CURRENT_TIMESTAMP")
    Page<Tournament> findPastTournaments(Pageable pageable);
    @Query("SELECT t FROM Tournament t WHERE CURRENT_TIMESTAMP BETWEEN t.startDateTime AND t.endDate")
    Page<Tournament> findCurrentTournaments(Pageable pageable);
    @Query("SELECT t FROM Tournament t WHERE t.startDateTime > CURRENT_TIMESTAMP")
    Page<Tournament> findUpcomingTournaments(Pageable pageable);
    @Query("SELECT t FROM Tournament t WHERE t.startDateTime BETWEEN :start AND :end")
    Page<Tournament> findTournamentsBetweenDates(Pageable pageable, @Param("start") LocalDateTime startDate, @Param("end") LocalDateTime endDate);

    @Query("SELECT t FROM Tournament t WHERE t.endDate < CURRENT_TIMESTAMP AND :user MEMBER OF t.soloParticipants")
    Page<Tournament> findPastTournamentsByUser(Pageable pageable, @Param("user") User user);
    @Query("SELECT t FROM Tournament t WHERE t.startDateTime <= CURRENT_TIMESTAMP AND t.endDate >= CURRENT_TIMESTAMP AND :user MEMBER OF t.soloParticipants")
    Page<Tournament> findCurrentTournamentsByUser(Pageable pageable, @Param("user") User user);
    @Query("SELECT t FROM Tournament t WHERE t.startDateTime > CURRENT_TIMESTAMP AND :user MEMBER OF t.soloParticipants")
    Page<Tournament> findUpcomingTournamentsByUser(Pageable pageable, @Param("user") User user);
    @Query("SELECT t FROM Tournament t WHERE t.startDateTime BETWEEN :start AND :end AND :user MEMBER OF t.soloParticipants")
    Page<Tournament> findTournamentsBetweenDatesByUser(Pageable pageable, @Param("start") LocalDateTime startDate, @Param("end") LocalDateTime endDate, @Param("user") User user);

    @Query("SELECT t FROM Tournament t WHERE t.endDate < CURRENT_TIMESTAMP AND :team MEMBER OF t.teamParticipants")
    Page<Tournament> findPastTournamentsByTeam(Pageable pageable, @Param("team") Team team);
    @Query("SELECT t FROM Tournament t WHERE t.startDateTime <= CURRENT_TIMESTAMP AND t.endDate >= CURRENT_TIMESTAMP AND :team MEMBER OF t.teamParticipants")
    Page<Tournament> findCurrentTournamentsByTeam(Pageable pageable, @Param("team") Team team);
    @Query("SELECT t FROM Tournament t WHERE t.startDateTime > CURRENT_TIMESTAMP AND :team MEMBER OF t.teamParticipants")
    Page<Tournament> findUpcomingTournamentsByTeam(Pageable pageable, @Param("team") Team team);
    @Query("SELECT t FROM Tournament t WHERE t.startDateTime BETWEEN :start AND :end AND :team MEMBER OF t.teamParticipants")
    Page<Tournament> findTournamentsBetweenDatesByTeam(Pageable pageable, @Param("start") LocalDateTime startDate, @Param("end") LocalDateTime endDate, @Param("team") Team team);
}

