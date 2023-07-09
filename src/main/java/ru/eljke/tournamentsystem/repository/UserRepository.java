package ru.eljke.tournamentsystem.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.eljke.tournamentsystem.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
    @Query(value = "SELECT u FROM user_details u " +
            "WHERE u.firstname LIKE %:keyword% " +
            "OR u.lastname LIKE %:keyword% " +
            "OR u.patronymic LIKE %:keyword% " +
            "OR u.city LIKE %:keyword% " +
            "OR u.school LIKE %:keyword% " +
            "OR u.grade LIKE %:keyword%")
    List<User> searchUsers(@Param("keyword") String keyword);
}
