package ru.eljke.tournamentsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.eljke.tournamentsystem.dto.LoginRequestDTO;
import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.entity.GradeLetter;
import ru.eljke.tournamentsystem.entity.GradeNumber;
import ru.eljke.tournamentsystem.mapper.UserMapper;
import ru.eljke.tournamentsystem.entity.User;
import ru.eljke.tournamentsystem.entity.Role;
import ru.eljke.tournamentsystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository repository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDTO getById(Long id) {
        if (repository.findById(id).isPresent()) {
            return UserMapper.INSTANCE.userToUserDTO(repository.findById(id).get());
        } else {
            return null;
        }
    }

    @Override
    public User getByUsername(String username) {
        if (repository.findUserByUsername(username) != null) {
            return repository.findUserByUsername(username);
        } else {
            return null;
        }
    }

    @Override
    public Page<UserDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(UserMapper.INSTANCE::userToUserDTO);
    }

    @Override
    public UserDTO create(User user) {
        if (repository.findUserByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username is already taken!");
        }
        if (user.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        if (user.getGradeNumber() != null && user.getGradeLetter() != null) {
            user.setGrade(user.getGrade());
        }
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>(user.getRoles());
            user.setRoles(roles);
        }

        return UserMapper.INSTANCE.userToUserDTO(repository.save(user));
    }

    @Override
    public UserDTO update(User user, Long id) {
        Optional<User> existingMemberOptional = repository.findById(id);
        if (existingMemberOptional.isPresent()) {
            User existingUser = existingMemberOptional.get();
            if (user.getUsername() != null) {
                existingUser.setUsername(user.getUsername());
            }
            if (user.getFirstname() != null) {
                existingUser.setFirstname(user.getFirstname());
            }
            if (user.getLastname() != null) {
                existingUser.setLastname(user.getLastname());
            }
            if (user.getPatronymic() != null) {
                existingUser.setPatronymic(user.getPatronymic());
            }
            if (user.getBirthDate() != null) {
                existingUser.setBirthDate(user.getBirthDate());
            }
            if (user.getPhone() != null) {
                existingUser.setPhone(user.getPhone());
            }
            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null) {
                existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
            if (user.getCity() != null) {
                existingUser.setCity(user.getCity());
            }
            if (user.getSchool() != null) {
                existingUser.setSchool(user.getSchool());
            }
            if (user.getGradeNumber() != null) {
                existingUser.setGradeNumber(user.getGradeNumber());
            }
            if (user.getGradeLetter() != null) {
                existingUser.setGradeLetter(user.getGradeLetter());
            }
            if (user.getGradeNumber() != null && user.getGradeLetter() != null) {
                existingUser.setGrade(user.getGrade());
            }

            return UserMapper.INSTANCE.userToUserDTO(repository.save(existingUser));
        } else {
            return null;
        }
    }

    @Override
    public void banById(Long id, String reason) {
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.ban(reason);

        repository.save(user);
    }

    @Override
    public void unbanById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.unban();

        repository.save(user);
    }

    @Override
    public void banByUsername(String username, String reason) {
        User user = repository.findUserByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        user.ban(reason);

        repository.save(user);
    }

    @Override
    public void unbanByUsername(String username) {
        User user = repository.findUserByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        user.unban();

        repository.save(user);
    }

    @Override
    public void delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (!user.isEnabled()) {
            throw new DisabledException("User is banned, reason: '" + user.getBanReason() + "'");
        }

        return user;
    }

    @Override
    public List<UserDTO> searchEverywhere(String keyword) {
        if (keyword != null) {
            return UserMapper.INSTANCE.usersToUserDTOs(repository.searchUsers(keyword));
        }
        throw new IllegalArgumentException("Keyword param is missing");
    }

    @Override
    public List<UserDTO> searchByParam(String param, String keyword) {
        if (keyword != null) {
            Specification<User> specification = byColumnNameAndValue(param, keyword);
            return UserMapper.INSTANCE.usersToUserDTOs(repository.findAll(specification));
        }
        throw new IllegalArgumentException("Keyword param is missing");
    }

    @Override
    public UserDTO register(LoginRequestDTO request) {
        User user = new User();
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        } else {
            throw new UnsupportedOperationException("Username cannot be null");
        }
        if (request.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        } else {
            throw new UnsupportedOperationException("Password cannot be null");
        }
        // TODO: ПОКА ТУТ ЗАГЛУШКА
        user.setCity("");
        user.setBirthDate(LocalDate.of(1970, 1, 1));
        user.setFirstname("");
        user.setLastname("");
        user.setPatronymic("");
        user.setSchool("");
        user.setGradeNumber(GradeNumber.ELEVEN);
        user.setGradeLetter(GradeLetter.А);
        user.setGrade(user.getGrade());
        user.setRoles(Collections.emptySet());

        return UserMapper.INSTANCE.userToUserDTO(repository.save(user));
    }

    @Override
    public boolean isOldPasswordCorrect(User user, String password) {
        return bCryptPasswordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        repository.save(user);
    }

    private static Specification<User> byColumnNameAndValue(String columnName, String value) {
        return (root, query, builder) -> {
            try {
                return builder.like(root.get(columnName), "%" + value + "%");
            } catch (IllegalArgumentException e) {
                // Обработка ошибки при несуществующем имени столбца
//                return builder.disjunction();
                throw new IllegalArgumentException("Incorrect param provided");
            }
        };
    }
}
