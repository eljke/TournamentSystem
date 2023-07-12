package ru.eljke.tournamentsystem.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Password change request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @Schema(name = "Current user's password", requiredMode = Schema.RequiredMode.REQUIRED)
    private String currentPassword;
    @Schema(name = "New user's password", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;
    @Schema(name = "Confirmation of new user's password", requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Must match the new one")
    private String confirmPassword;
}
