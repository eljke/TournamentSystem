package ru.eljke.tournamentsystem.service;

import ru.eljke.tournamentsystem.model.Member;
import ru.eljke.tournamentsystem.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServiceImplTest {
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
    void testGetAll() {
        List<Member> expectedMembers = new ArrayList<>();
        expectedMembers.add(new Member());
        expectedMembers.add(new Member());
        when(repository.findAll(any(Sort.class))).thenReturn(expectedMembers);

        List<Member> actualMembers = memberService.getAll();

        assertEquals(expectedMembers.size(), actualMembers.size());
    }

    @Test
    void testFindAllPageable() {
        Member member1 = new Member(1L, "user1", "John", "Doe", "Patronymic1",
                LocalDate.of(2000, 12, 12), "1234567890", "john@example.com",
                "password", "Moscow", "Test School 1", "Test Grade 1");
        Member member2 = new Member(2L, "user2", "Alice", "Smith", "Patronymic2",
                LocalDate.of(2002, 5, 28), "1234567890", "alice@example.com",
                "password", "Boston", "Test School 2", "Test Grade 2");

        List<Member> members = Arrays.asList(member1, member2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        // Создаем объект Page с помощью конструктора PageImpl
        Page<Member> page = new PageImpl<>(members, pageable, members.size());

        when(repository.findAll(pageable)).thenReturn(page);

        Page<Member> resultPage = memberService.findAllPageable(pageable);

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
