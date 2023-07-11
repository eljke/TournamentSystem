package ru.eljke.tournamentsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.eljke.tournamentsystem.dto.LoginRequestDTO;
import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.entity.User;

import java.util.List;

public interface UserService {
    UserDTO getById(Long id);
    User getByUsername(String username);
    Page<UserDTO> getAll(Pageable pageable);
    UserDTO create(User user);
    UserDTO update(User user, Long id);
    void banById(Long id, String reason);
    void unbanById(Long id);
    void banByUsername(String username, String reason);
    void unbanByUsername(String username);
    void delete(Long id);
    List<UserDTO> searchEverywhere(String keyword);
    List<UserDTO> searchByParam(String param, String keyword);
    UserDTO register(LoginRequestDTO request);
    boolean isOldPasswordCorrect(User user, String password);
    void changePassword(User user, String newPassword);
}
