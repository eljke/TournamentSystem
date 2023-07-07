package ru.eljke.tournamentsystem.controller;

import ru.eljke.tournamentsystem.model.GradeLetter;
import ru.eljke.tournamentsystem.model.GradeNumber;
import ru.eljke.tournamentsystem.model.Member;
import ru.eljke.tournamentsystem.model.Role;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class AdminControllerTest {

    @Mock
    private MemberServiceImpl service;

    @InjectMocks
    private AdminController controller;

    private MockMvc mockMvc;
    ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testFindAll() throws Exception {
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
        member1.setGradeNumber(GradeNumber.TEN);
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
        member2.setGradeNumber(GradeNumber.NINE);
        member2.setGradeLetter(GradeLetter.Б);
        member2.setRoles(Collections.singleton(Role.STUDENT));

        List<Member> members = Arrays.asList(member1, member2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Member> page = new PageImpl<>(members, pageable, members.size());

        when(service.getAll(pageable)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(members.size()))

                .andExpect(jsonPath("$.content[0].id").value(member1.getId()))
                .andExpect(jsonPath("$.content[0].username").value(member1.getUsername()))
                .andExpect(jsonPath("$.content[0].firstname").value(member1.getFirstname()))
                .andExpect(jsonPath("$.content[0].lastname").value(member1.getLastname()))
                .andExpect(jsonPath("$.content[0].patronymic").value(member1.getPatronymic()))
                .andExpect(jsonPath("$.content[0].birthDate").value(member1.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
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
                .andExpect(jsonPath("$.content[1].birthDate").value(member2.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(jsonPath("$.content[1].phone").value(member2.getPhone()))
                .andExpect(jsonPath("$.content[1].email").value(member2.getEmail()))
                .andExpect(jsonPath("$.content[1].password").value(member2.getPassword()))
                .andExpect(jsonPath("$.content[1].city").value(member2.getCity()))
                .andExpect(jsonPath("$.content[1].school").value(member2.getSchool()))
                .andExpect(jsonPath("$.content[1].grade").value(member2.getGrade()));
    }

    @Test
    void testGetById() throws Exception {
        Member expectedMember = new Member();
        expectedMember.setId(1L);
        expectedMember.setFirstname("firstname");
        expectedMember.setGradeLetter(GradeLetter.А);
        expectedMember.setGradeNumber(GradeNumber.ELEVEN);

        when(service.getById(any(Long.class))).thenReturn(Optional.of(expectedMember));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/members/{id}", expectedMember.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedMember.getId()))
                .andExpect(jsonPath("$.firstname").value(expectedMember.getFirstname()));
    }

    @Test
    void testCreate() throws Exception {
        Member memberToCreate = new Member();
        memberToCreate.setId(1L);
        memberToCreate.setUsername("user1");
        memberToCreate.setFirstname("John");
        memberToCreate.setLastname("Doe");
        memberToCreate.setPatronymic("Patronymic1");
        memberToCreate.setBirthDate(LocalDate.of(2000, 12, 12));
        memberToCreate.setPhone("1234567890");
        memberToCreate.setEmail("john@example.com");
        memberToCreate.setPassword("password");
        memberToCreate.setCity("Moscow");
        memberToCreate.setSchool("Test School 1");
        memberToCreate.setGradeNumber(GradeNumber.TEN);
        memberToCreate.setGradeLetter(GradeLetter.А);
        memberToCreate.setRoles(Collections.singleton(Role.STUDENT));

        Member createdMember = new Member();
        createdMember.setId(1L);
        createdMember.setUsername("user1");
        createdMember.setFirstname("John");
        createdMember.setLastname("Doe");
        createdMember.setPatronymic("Patronymic1");
        createdMember.setBirthDate(LocalDate.of(2000, 12, 12));
        createdMember.setPhone("1234567890");
        createdMember.setEmail("john@example.com");
        createdMember.setPassword("password");
        createdMember.setCity("Moscow");
        createdMember.setSchool("Test School 1");
        createdMember.setGradeNumber(GradeNumber.TEN);
        createdMember.setGradeLetter(GradeLetter.А);
        createdMember.setRoles(Collections.singleton(Role.STUDENT));

        when(service.create(any(Member.class))).thenReturn(createdMember);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/members/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberToCreate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdMember.getId()))
                .andExpect(jsonPath("$.username").value(createdMember.getUsername()))
                .andExpect(jsonPath("$.firstname").value(createdMember.getFirstname()))
                .andExpect(jsonPath("$.lastname").value(createdMember.getLastname()))
                .andExpect(jsonPath("$.patronymic").value(createdMember.getPatronymic()))
                .andExpect(jsonPath("$.birthDate").value(createdMember.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
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
        Member memberToUpdate = new Member();
        memberToUpdate.setId(1L);
        memberToUpdate.setUsername("user1");
        memberToUpdate.setFirstname("John");
        memberToUpdate.setLastname("Doe");
        memberToUpdate.setPatronymic("Patronymic1");
        memberToUpdate.setBirthDate(LocalDate.of(2000, 12, 12));
        memberToUpdate.setPhone("1234567890");
        memberToUpdate.setEmail("john@example.com");
        memberToUpdate.setPassword("password");
        memberToUpdate.setCity("Moscow");
        memberToUpdate.setSchool("Test School 1");
        memberToUpdate.setGradeNumber(GradeNumber.TEN);
        memberToUpdate.setGradeLetter(GradeLetter.А);
        memberToUpdate.setRoles(Collections.singleton(Role.STUDENT));

        Member updatedMember = new Member();
        updatedMember.setId(1L);
        updatedMember.setUsername("updated_user");
        updatedMember.setFirstname("updated_firstname");
        updatedMember.setLastname("updated_lastname");
        updatedMember.setPatronymic("updated_patronymic");
        updatedMember.setBirthDate(LocalDate.of(2002, 7, 5));
        updatedMember.setPhone("updated_phone");
        updatedMember.setEmail("john@example.com");
        updatedMember.setPassword("updated_password");
        updatedMember.setCity("updated_city");
        updatedMember.setSchool("updated_school");
        updatedMember.setGradeNumber(GradeNumber.NINE);
        updatedMember.setGradeLetter(GradeLetter.В);
        updatedMember.setRoles(Collections.singleton(Role.STUDENT));

        updatedMember.setId(memberId);

        when(service.getById(memberId)).thenReturn(Optional.of(updatedMember));
        when(service.update(any(Member.class), eq(memberId))).thenReturn(updatedMember);

        mockMvc.perform(MockMvcRequestBuilders.put("/admin/members/{id}", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(updatedMember.getId()))
                .andExpect(jsonPath("$.username").value(updatedMember.getUsername()))
                .andExpect(jsonPath("$.firstname").value(updatedMember.getFirstname()))
                .andExpect(jsonPath("$.lastname").value(updatedMember.getLastname()))
                .andExpect(jsonPath("$.patronymic").value(updatedMember.getPatronymic()))
                .andExpect(jsonPath("$.birthDate").value(updatedMember.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
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

        when(service.getById(memberId)).thenReturn(Optional.of(expectedMember));

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/members/{id}", memberId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string("Успешно!"));

        verify(service, times(1)).delete(memberId);
    }

}
