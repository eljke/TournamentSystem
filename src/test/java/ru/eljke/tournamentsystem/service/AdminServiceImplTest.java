package ru.eljke.tournamentsystem.service;

import ru.eljke.tournamentsystem.model.GradeLetter;
import ru.eljke.tournamentsystem.model.GradeNumber;
import ru.eljke.tournamentsystem.model.Member;
import ru.eljke.tournamentsystem.model.Role;
import ru.eljke.tournamentsystem.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {
    @Mock
    private MemberRepository repository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById() {
        Long memberId = 1L;
        Member expectedMember = new Member();
        expectedMember.setId(memberId);
        when(repository.findById(memberId)).thenReturn(Optional.of(expectedMember));

        Optional<Member> actualMember = memberService.getById(memberId);

        assertEquals(expectedMember, actualMember.orElse(null));
    }

    @Test
    void testFindAll() {
        Member member1 = new Member();
        member1.setId(1L);
        member1.setUsername("user1");
        member1.setFirstname("John");
        member1.setLastname("Doe");
        member1.setPatronymic("Patronymic1");
        member1.setBirthDate(LocalDate.of(2000, 12, 12));
        member1.setPhone("1234567890");
        member1.setEmail("john@example.com");
        member1.setPassword("password");
        member1.setCity("Moscow");
        member1.setSchool("Test School 1");
        member1.setGradeNumber(GradeNumber.ELEVEN);
        member1.setGradeLetter(GradeLetter.А);
        member1.setRoles(Collections.singleton(Role.STUDENT));

        Member member2 = new Member();
        member2.setId(2L);
        member2.setUsername("user2");
        member2.setFirstname("Alice");
        member2.setLastname("Smith");
        member2.setPatronymic("Patronymic2");
        member2.setBirthDate(LocalDate.of(2002, 5, 28));
        member2.setPhone("1234567890");
        member2.setEmail("alice@example.com");
        member2.setPassword("password");
        member2.setCity("Boston");
        member2.setSchool("Test School 2");
        member2.setGradeNumber(GradeNumber.EIGHT);
        member2.setGradeLetter(GradeLetter.Г);
        member2.setRoles(Collections.singleton(Role.STUDENT));


        List<Member> members = Arrays.asList(member1, member2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        // Создаем объект Page с помощью конструктора PageImpl
        Page<Member> page = new PageImpl<>(members, pageable, members.size());

        when(repository.findAll(pageable)).thenReturn(page);

        Page<Member> resultPage = memberService.getAll(pageable);

        assertEquals(members.size(), resultPage.getTotalElements());
        assertEquals(members, resultPage.getContent());
        assertEquals(pageable, resultPage.getPageable());
    }

    @Test
    void testCreate() {
        Member memberToCreate = new Member();
        when(repository.save(any(Member.class))).thenReturn(memberToCreate);

        Member createdMember = memberService.create(memberToCreate);

        assertEquals(memberToCreate, createdMember);
        verify(repository, times(1)).save(memberToCreate);
    }

    @Test
    void testUpdate() {
        Long memberId = 1L;
        Member existingMember = new Member();
        existingMember.setId(memberId);

        Member updatedMemberData = new Member();
        updatedMemberData.setFirstname("John");
        updatedMemberData.setLastname("Doe");

        when(repository.findById(memberId)).thenReturn(Optional.of(existingMember));
        when(repository.save(any(Member.class))).thenReturn(existingMember);

        Member updatedMember = memberService.update(updatedMemberData, memberId);

        assertEquals(updatedMemberData.getFirstname(), updatedMember.getFirstname());
        assertEquals(updatedMemberData.getLastname(), updatedMember.getLastname());
        verify(repository, times(1)).findById(memberId);
        verify(repository, times(1)).save(existingMember);
    }

    @Test
    void testUpdateNonExistingMember() {
        Long memberId = 1L;
        Member updatedMemberData = new Member();

        when(repository.findById(memberId)).thenReturn(Optional.empty());

        Member updatedMember = memberService.update(updatedMemberData, memberId);

        assertNull(updatedMember);
        verify(repository, times(1)).findById(memberId);
        verify(repository, never()).save(any(Member.class));
    }

    @Test
    void testDelete() {
        Long memberId = 1L;
        when(repository.findById(memberId)).thenReturn(Optional.of(new Member()));

        memberService.delete(memberId);

        verify(repository, times(1)).deleteById(memberId);
    }

    @Test
    void testDeleteNonExistingMember() {
        Long memberId = 1L;
        when(repository.findById(memberId)).thenReturn(Optional.empty());

        memberService.delete(memberId);

        verify(repository, never()).deleteById(any());
    }
}
