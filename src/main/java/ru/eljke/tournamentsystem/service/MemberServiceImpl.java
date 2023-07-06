package ru.eljke.tournamentsystem.service;

import ru.eljke.tournamentsystem.model.Member;
import ru.eljke.tournamentsystem.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements DatabaseService<Member> {
    private final MemberRepository repository;

    public MemberServiceImpl(MemberRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Member> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Member> getAll() {
        return repository.findAll(Sort.by("id"));
    }

    @Override
    public Page<Member> findAllPageable(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Member create(Member member) {
        return repository.save(member);
    }

    @Override
    public Member update(Member member, Long id) {
        Optional<Member> existingMemberOptional = repository.findById(id);
        if (existingMemberOptional.isPresent()) {
            Member existingMember = existingMemberOptional.get();
            existingMember.setUsername(member.getUsername());
            existingMember.setFirstname(member.getFirstname());
            existingMember.setLastname(member.getLastname());
            existingMember.setPatronymic(member.getPatronymic());
            existingMember.setDateOfBirth(member.getDateOfBirth());
            existingMember.setPhone(member.getPhone());
            existingMember.setEmail(member.getEmail());
            existingMember.setPassword(member.getPassword());
            existingMember.setCity(member.getCity());
            existingMember.setSchool(member.getSchool());
            existingMember.setGrade(member.getGrade());

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
}
