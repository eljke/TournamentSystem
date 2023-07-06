package ru.eljke.tournamentsystem.controller;

import ru.eljke.tournamentsystem.model.Member;
import ru.eljke.tournamentsystem.service.MemberServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class MemberControllerTest {

    @Mock
    private MemberServiceImpl memberService;

    @InjectMocks
    private MemberController memberController;

    private MockMvc mockMvc;
    ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    void testGetAll() throws Exception {
        Member member1 = new Member(1L, "user1", "John", "Doe", "Patronymic1",
                LocalDate.of(2000, 12, 12), "1234567890", "john@example.com",
                "password", "Moscow", "Test School 1", "Test Grade 1");
        Member member2 = new Member(2L, "user2", "Alice", "Smith", "Patronymic2",
                LocalDate.of(2002, 5, 28), "1234567890", "alice@example.com",
                "password", "Boston", "Test School 2", "Test Grade 2");

        List<Member> members = Arrays.asList(member1, member2);

        when(memberService.getAll()).thenReturn(members);

        mockMvc.perform(MockMvcRequestBuilders.get("/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(members.size()))

                .andExpect(jsonPath("$[0].id").value(member1.getId()))
                .andExpect(jsonPath("$[0].username").value(member1.getUsername()))
                .andExpect(jsonPath("$[0].firstname").value(member1.getFirstname()))
                .andExpect(jsonPath("$[0].lastname").value(member1.getLastname()))
                .andExpect(jsonPath("$[0].patronymic").value(member1.getPatronymic()))
                .andExpect(jsonPath("$[0].dateOfBirth").value(member1.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(jsonPath("$[0].phone").value(member1.getPhone()))
                .andExpect(jsonPath("$[0].email").value(member1.getEmail()))
                .andExpect(jsonPath("$[0].password").value(member1.getPassword()))
                .andExpect(jsonPath("$[0].city").value(member1.getCity()))
                .andExpect(jsonPath("$[0].school").value(member1.getSchool()))
                .andExpect(jsonPath("$[0].grade").value(member1.getGrade()))

                .andExpect(jsonPath("$[1].id").value(member2.getId()))
                .andExpect(jsonPath("$[1].username").value(member2.getUsername()))
                .andExpect(jsonPath("$[1].firstname").value(member2.getFirstname()))
                .andExpect(jsonPath("$[1].lastname").value(member2.getLastname()))
                .andExpect(jsonPath("$[1].patronymic").value(member2.getPatronymic()))
                .andExpect(jsonPath("$[1].dateOfBirth").value(member2.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(jsonPath("$[1].phone").value(member2.getPhone()))
                .andExpect(jsonPath("$[1].email").value(member2.getEmail()))
                .andExpect(jsonPath("$[1].password").value(member2.getPassword()))
                .andExpect(jsonPath("$[1].city").value(member2.getCity()))
                .andExpect(jsonPath("$[1].school").value(member2.getSchool()))
                .andExpect(jsonPath("$[1].grade").value(member2.getGrade()));
    }

    @Test
    void testFindAllPageable() throws Exception {
        Member member1 = new Member(1L, "user1", "John", "Doe", "Patronymic1",
                LocalDate.of(2000, 12, 12), "1234567890", "john@example.com",
                "password", "Moscow", "Test School 1", "Test Grade 1");
        Member member2 = new Member(2L, "user2", "Alice", "Smith", "Patronymic2",
                LocalDate.of(2002, 5, 28), "1234567890", "alice@example.com",
                "password", "Boston", "Test School 2", "Test Grade 2");

        List<Member> members = Arrays.asList(member1, member2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Member> page = new PageImpl<>(members, pageable, members.size());

        when(memberService.getAllPageable(pageable)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/members/pages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(members.size()))

                .andExpect(jsonPath("$.content[0].id").value(member1.getId()))
                .andExpect(jsonPath("$.content[0].username").value(member1.getUsername()))
                .andExpect(jsonPath("$.content[0].firstname").value(member1.getFirstname()))
                .andExpect(jsonPath("$.content[0].lastname").value(member1.getLastname()))
                .andExpect(jsonPath("$.content[0].patronymic").value(member1.getPatronymic()))
                .andExpect(jsonPath("$.content[0].dateOfBirth").value(member1.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(jsonPath("$.content[0].phone").value(member1.getPhone()))
                .andExpect(jsonPath("$.content[0].email").value(member1.getEmail()))
                .andExpect(jsonPath("$.content[0].password").value(member1.getPassword()))
                .andExpect(jsonPath("$.content[0].city").value(member1.getCity()))
                .andExpect(jsonPath("$.content[0].school").value(member1.getSchool()))
                .andExpect(jsonPath("$.content[0].grade").value(member1.getGrade()))

                .andExpect(jsonPath("$.content[1].id").value(member2.getId()))
                .andExpect(jsonPath("$.content[1].username").value(member2.getUsername()))
                .andExpect(jsonPath("$.content[1].firstname").value(member2.getFirstname()))
                .andExpect(jsonPath("$.content[1].lastname").value(member2.getLastname()))
                .andExpect(jsonPath("$.content[1].patronymic").value(member2.getPatronymic()))
                .andExpect(jsonPath("$.content[1].dateOfBirth").value(member2.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(jsonPath("$.content[1].phone").value(member2.getPhone()))
                .andExpect(jsonPath("$.content[1].email").value(member2.getEmail()))
                .andExpect(jsonPath("$.content[1].password").value(member2.getPassword()))
                .andExpect(jsonPath("$.content[1].city").value(member2.getCity()))
                .andExpect(jsonPath("$.content[1].school").value(member2.getSchool()))
                .andExpect(jsonPath("$.content[1].grade").value(member2.getGrade()));
    }

    @Test
    void testGetById() throws Exception {
        Long memberId = 1L;
        Member expectedMember = new Member();
        expectedMember.setId(memberId);

        when(memberService.getById(memberId)).thenReturn(Optional.of(expectedMember));

        mockMvc.perform(MockMvcRequestBuilders.get("/members/{id}", memberId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedMember.getId()));
    }

    @Test
    void testCreate() throws Exception {
        Member memberToCreate = new Member(1L, "user1", "John", "Doe", "Patronymic1",
                LocalDate.of(2000, 12, 12), "1234567890", "john@example.com",
                "password", "Moscow", "Test School 1", "Test Grade 1");
        Member createdMember = new Member(1L, "user1", "John", "Doe", "Patronymic1",
                LocalDate.of(2000, 12, 12), "1234567890", "john@example.com",
                "password", "Moscow", "Test School 1", "Test Grade 1");
        createdMember.setId(1L);

        when(memberService.create(any(Member.class))).thenReturn(createdMember);

        mockMvc.perform(MockMvcRequestBuilders.post("/members/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberToCreate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdMember.getId()))
                .andExpect(jsonPath("$.username").value(createdMember.getUsername()))
                .andExpect(jsonPath("$.firstname").value(createdMember.getFirstname()))
                .andExpect(jsonPath("$.lastname").value(createdMember.getLastname()))
                .andExpect(jsonPath("$.patronymic").value(createdMember.getPatronymic()))
                .andExpect(jsonPath("$.dateOfBirth").value(createdMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(jsonPath("$.phone").value(createdMember.getPhone()))
                .andExpect(jsonPath("$.email").value(createdMember.getEmail()))
                .andExpect(jsonPath("$.password").value(createdMember.getPassword()))
                .andExpect(jsonPath("$.city").value(createdMember.getCity()))
                .andExpect(jsonPath("$.school").value(createdMember.getSchool()))
                .andExpect(jsonPath("$.grade").value(createdMember.getGrade()));
    }

    @Test
    void testUpdate() throws Exception {
        Long memberId = 1L;
        Member memberToUpdate = new Member(1L, "user1", "John", "Doe", "Patronymic1",
                LocalDate.of(2000, 12, 12), "1234567890", "john@example.com",
                "password", "Moscow", "Test School 1", "Test Grade 1");

        Member updatedMember = new Member(1L, "updated_user", "updated_firstname", "updated_lastname", "updated_patronymic",
                LocalDate.of(2002, 7, 5), "updated_phone", "john@example.com",
                "updated_password", "updated_city", "updated_school", "updated_grade");
        updatedMember.setId(memberId);

        when(memberService.getById(memberId)).thenReturn(Optional.of(updatedMember));
        when(memberService.update(any(Member.class), eq(memberId))).thenReturn(updatedMember);

        mockMvc.perform(MockMvcRequestBuilders.put("/members/{id}", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(updatedMember.getId()))
                .andExpect(jsonPath("$.username").value(updatedMember.getUsername()))
                .andExpect(jsonPath("$.firstname").value(updatedMember.getFirstname()))
                .andExpect(jsonPath("$.lastname").value(updatedMember.getLastname()))
                .andExpect(jsonPath("$.patronymic").value(updatedMember.getPatronymic()))
                .andExpect(jsonPath("$.dateOfBirth").value(updatedMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(jsonPath("$.phone").value(updatedMember.getPhone()))
                .andExpect(jsonPath("$.email").value(updatedMember.getEmail()))
                .andExpect(jsonPath("$.password").value(updatedMember.getPassword()))
                .andExpect(jsonPath("$.city").value(updatedMember.getCity()))
                .andExpect(jsonPath("$.school").value(updatedMember.getSchool()))
                .andExpect(jsonPath("$.grade").value(updatedMember.getGrade()));
    }

    @Test
    void testDelete() throws Exception {
        Long memberId = 1L;
        Member expectedMember = new Member();
        expectedMember.setId(memberId);

        when(memberService.getById(memberId)).thenReturn(Optional.of(expectedMember));

        mockMvc.perform(MockMvcRequestBuilders.delete("/members/{id}", memberId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string("Успешно!"));

        verify(memberService, times(1)).delete(memberId);
    }

}
