package int221.sit.taskboard.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class JwtRequestUser {
    @NotNull
    @NotBlank
    @Size(max = 50, message = "username is incorrect")
    private String userName;

    @NotNull
    @NotBlank
    @Size(max = 14, message = "password is incorrect")
    private String password;

    public String getUserName() {
        return userName != null ? userName.trim() : null;
    }
    public String getPassword() {
        return password != null ? password.trim() : null;
    }
}
