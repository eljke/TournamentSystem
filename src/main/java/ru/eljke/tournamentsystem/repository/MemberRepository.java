package ru.eljke.tournamentsystem.repository;

import ru.eljke.tournamentsystem.model.Member;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
