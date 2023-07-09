package ru.eljke.tournamentsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String username;
    private String fullname;
    private String birthDate;
    private String city;
    private String school;
    private String grade;
    private String roles;
}
