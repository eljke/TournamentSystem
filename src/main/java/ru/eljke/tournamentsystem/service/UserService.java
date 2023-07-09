package ru.eljke.tournamentsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.eljke.tournamentsystem.model.User;

import java.util.List;

public interface UserService {
    User getById(Long id);
    User getByUsername(String username);
    Page<User> getAll(Pageable pageable);
    User create(User user);
    User update(User user, Long id);
    void banById(Long id, String reason);
    void unbanById(Long id);
    void banByUsername(String username, String reason);
    void unbanByUsername(String username);
    void delete(Long id);
    List<User> search(String param, String keyword);
    boolean isOldPasswordCorrect(User user, String password);
    void changePassword(User user, String newPassword);
}
