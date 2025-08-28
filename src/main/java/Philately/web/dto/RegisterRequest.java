package Philately.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 symbols!")
    private String username;

    @Email(message = "Invalid email!")
    private String email;

    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 symbols!")
    private String password;

    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 symbols!")
    private String confirmPassword;
}
