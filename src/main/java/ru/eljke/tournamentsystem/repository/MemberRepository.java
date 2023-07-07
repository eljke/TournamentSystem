package ru.eljke.tournamentsystem.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.eljke.tournamentsystem.model.Member;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByUsername(String username);
    @Query(value = "SELECT * FROM Member m " +
            "WHERE m.firstname LIKE %:keyword% " +
            "OR m.lastname LIKE %:keyword% " +
            "OR m.patronymic LIKE %:keyword% " +
            "OR m.city LIKE %:keyword% " +
            "OR m.school LIKE %:keyword% " +
            "OR m.grade LIKE %:keyword%",
            nativeQuery = true)
    List<Member> searchMembers(@Param("keyword") String keyword);

}
