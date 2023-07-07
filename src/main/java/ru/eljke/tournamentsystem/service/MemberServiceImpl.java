package ru.eljke.tournamentsystem.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.eljke.tournamentsystem.model.GradeLetter;
import ru.eljke.tournamentsystem.model.GradeNumber;
import ru.eljke.tournamentsystem.model.Member;
import ru.eljke.tournamentsystem.model.Role;
import ru.eljke.tournamentsystem.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements UserDetailsService, DatabaseService<Member>, SearchService<Member> {
    private final MemberRepository repository;
    private final EntityManager entityManager;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Optional<Member> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Member> getByParam(String username) {
        return repository.findMemberByUsername(username);
    }

    @Override
    public Page<Member> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Member create(Member member) {
        if (member.getPassword() != null) {
            member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        }
        if (member.getGradeNumber() != null && member.getGradeLetter() != null) {
            member.setGrade(member.getGrade());
        }
        if (member.getRoles() != null && !member.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>(member.getRoles());
            member.setRoles(roles);
        }

        return repository.save(member);
    }

    @Override
    public Member update(Member member, Long id) {
        Optional<Member> existingMemberOptional = repository.findById(id);
        if (existingMemberOptional.isPresent()) {
            Member existingMember = existingMemberOptional.get();
            if (member.getUsername() != null) {
                existingMember.setUsername(member.getUsername());
            }
            if (member.getFirstname() != null) {
                existingMember.setFirstname(member.getFirstname());
            }
            if (member.getLastname() != null) {
                existingMember.setLastname(member.getLastname());
            }
            if (member.getPatronymic() != null) {
                existingMember.setPatronymic(member.getPatronymic());
            }
            if (member.getBirthDate() != null) {
                existingMember.setBirthDate(member.getBirthDate());
            }
            if (member.getPhone() != null) {
                existingMember.setPhone(member.getPhone());
            }
            if (member.getEmail() != null) {
                existingMember.setEmail(member.getEmail());
            }
            if (member.getPassword() != null) {
                existingMember.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
            }
            if (member.getCity() != null) {
                existingMember.setCity(member.getCity());
            }
            if (member.getSchool() != null) {
                existingMember.setSchool(member.getSchool());
            }
            if (member.getGradeNumber() != null) {
                existingMember.setGradeNumber(member.getGradeNumber());
            }
            if (member.getGradeLetter() != null) {
                existingMember.setGradeLetter(member.getGradeLetter());
            }
            if (member.getGradeNumber() != null && member.getGradeLetter() != null) {
                existingMember.setGrade(member.getGrade());
            }

            return repository.save(existingMember);
        } else {
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findMemberByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public List<Member> search(String param, String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        // Создаём типизированный запрос, который определяет результаты и структуру запроса
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        // Создаём корневой элемент запроса, который соответствует таблице в базе данных
        Root<Member> root = criteriaQuery.from(Member.class);

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
            // Проверяем, существует ли поле param в сущности Member
            try {
                if (param == null) {
                    return repository.searchMembers(keyword);
                } else {
                    root.get(param);
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

    public void register(Member user) {
        Optional<Member> userFromDatabase = repository.findMemberByUsername(user.getUsername());

        if (userFromDatabase.isPresent()) {
            return;
        }

        user.setFirstname("");
        user.setLastname("");
        user.setPatronymic("");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setPhone("");
        user.setEmail("");
        user.setCity("");
        user.setSchool("");
        user.setGradeNumber(GradeNumber.NINE);
        user.setGradeLetter(GradeLetter.А);
        user.setGrade(user.getGrade());
        user.setRoles(Collections.singleton(user.getRoles().iterator().next()));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        repository.save(user);
    }
}
