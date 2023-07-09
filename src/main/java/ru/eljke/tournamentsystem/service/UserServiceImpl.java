package ru.eljke.tournamentsystem.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.eljke.tournamentsystem.model.User;
import ru.eljke.tournamentsystem.model.Role;
import ru.eljke.tournamentsystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository repository;
    private final EntityManager entityManager;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User getById(Long id) {
        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
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
    public Page<User> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public User create(User user) {
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

        return repository.save(user);
    }

    @Override
    public User update(User user, Long id) {
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

            return repository.save(existingUser);
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
        user.ban(reason);

        repository.save(user);
    }

    @Override
    public void unbanByUsername(String username) {
        User user = repository.findUserByUsername(username);
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
    public List<User> search(String param, String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        // Создаём типизированный запрос, который определяет результаты и структуру запроса
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        // Создаём корневой элемент запроса, который соответствует таблице в базе данных
        Root<User> root = criteriaQuery.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (param != null && param.equals("fullname")) {
            // Создаем ФИО
            Expression<String> fullName = criteriaBuilder.concat(
                    criteriaBuilder.concat(
                            criteriaBuilder.concat(
                                    criteriaBuilder.concat(
                                            criteriaBuilder.trim(root.get("firstname")),
                                            " "),
                                    criteriaBuilder.trim(root.get("lastname"))),
                            " "),
                    criteriaBuilder.trim(root.get("patronymic"))
            );
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(fullName), "%" + keyword.toLowerCase() + "%"));
        } else {
            // Проверяем, существует ли поле param в сущности User
            try {
                if (param == null) {
                    return repository.searchUsers(keyword);
                } else {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(param)), "%" + keyword.toLowerCase() + "%"));
                }
            } catch (IllegalArgumentException e) {
                // Обработка случая, когда поле не существует
                // TODO: Наверное, стоит всё же по-нормальному обработать позже
                return Collections.emptyList();
            }
        }
        // Указываем предикаты в критерии запроса
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaQuery).getResultList();
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
}
