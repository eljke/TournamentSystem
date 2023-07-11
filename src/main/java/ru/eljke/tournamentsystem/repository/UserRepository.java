package ru.eljke.tournamentsystem.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.eljke.tournamentsystem.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findUserByUsername(String username);
    @Query(value = "SELECT u FROM user_details u " +
            "WHERE LOWER(CONCAT(u.lastname, ' ', u.firstname, ' ', u.patronymic)) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(u.city) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(u.school) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(u.grade) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchUsers(@Param("keyword") String keyword);
}
