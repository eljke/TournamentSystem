package ru.eljke.tournamentsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;


@Schema(description = "User details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "user_details")
public class User implements UserDetails {
    @Schema(name = "Autogenerated ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Schema(name = "User username", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "username", unique = true)
    @Size(min = 3, max = 16, message = "Username should be at least ${min} and max ${max} symbols")
    private String username;
    @Schema(name = "User firstname", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "firstname")
    private String firstname;
    @Schema(name = "User lastname", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "lastname")
    private String lastname;
    @Schema(name = "User patronymic", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "patronymic")
    private String patronymic;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @PastOrPresent
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Schema(name = "User phone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "phone")
    private String phone;
    @Schema(name = "User email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Email
    @Column(name = "email")
    private String email;
    @Schema(name = "User password", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "password")
    private String password;
    @Schema(name = "User city", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "city")
    private String city;
    @Schema(name = "User school", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "school")
    private String school;

    @Schema(name = "User grade number", requiredMode = Schema.RequiredMode.REQUIRED)
    @Enumerated(EnumType.STRING)
    @Column(name = "grade_number")
    private GradeNumber gradeNumber;
    @Schema(name = "User grade letter", requiredMode = Schema.RequiredMode.REQUIRED)
    @Enumerated(EnumType.STRING)
    @Column(name = "grade_letter")
    private GradeLetter gradeLetter;
    @Schema(name = "User grade", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "grade")
    private String grade;
    @Schema(name = "User roles", requiredMode = Schema.RequiredMode.REQUIRED)
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER, targetClass = Role.class)
    private Set<Role> roles;
    @Schema(name = "Is user banned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean isBanned = false;
    @Schema(name = "User's ban reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String banReason;

    public String getGrade() {
        if (getGradeNumber() != null && getGradeLetter() != null) {
            return String.valueOf(gradeNumber.getValue()) + gradeLetter;
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(firstname, user.firstname) && Objects.equals(lastname, user.lastname) && Objects.equals(patronymic, user.patronymic) && Objects.equals(birthDate, user.birthDate) && Objects.equals(phone, user.phone) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(city, user.city) && Objects.equals(school, user.school) && gradeNumber == user.gradeNumber && gradeLetter == user.gradeLetter && Objects.equals(grade, user.grade) && Objects.equals(roles, user.roles) && Objects.equals(isBanned, user.isBanned) && Objects.equals(banReason, user.banReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstname, lastname, patronymic, birthDate, phone, email, password, city, school, gradeNumber, gradeLetter, grade, roles, isBanned, banReason);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return !isBanned;
    }
    @JsonIgnore
    public void ban(String reason) {
        this.isBanned = true;
        this.banReason = reason;
    }
    @JsonIgnore
    public void unban() {
        this.isBanned = false;
    }

    @JsonIgnore
    public String getFullName() {
        StringBuilder fullNameBuilder = new StringBuilder();

        if (lastname != null && !lastname.trim().isEmpty()) {
            fullNameBuilder.append(lastname);
        }

        if (firstname != null && !firstname.trim().isEmpty()) {
            if (fullNameBuilder.length() > 0) {
                fullNameBuilder.append(" ");
            }
            fullNameBuilder.append(firstname);
        }

        if (patronymic != null && !patronymic.trim().isEmpty()) {
            if (fullNameBuilder.length() > 0) {
                fullNameBuilder.append(" ");
            }
            fullNameBuilder.append(patronymic);
        }

        String fullName = fullNameBuilder.toString();

        if (fullName.isEmpty()) {
            if (this.username != null) {
                return this.username;
            }
            return null;
        } else {
            return fullName;
        }
    }
}
