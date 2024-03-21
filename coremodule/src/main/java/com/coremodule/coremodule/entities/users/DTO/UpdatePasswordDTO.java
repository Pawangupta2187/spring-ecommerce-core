package com.coremodule.coremodule.entities.users.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Service
@Getter
@Setter
@NoArgsConstructor

@Data
public class UpdatePasswordDTO {

    @NotEmpty(message = "new password can't be empty")
    @Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message="Password must be in format")
    private String password;
    @NotEmpty(message = "confirm password can't be empty")
    private String confirmPassword;
}
