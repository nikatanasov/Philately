package Philately.web.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 symbols!")
    private String username;

    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 symbols!")
    private String password;
}
