package int221.sit.taskboard.DTO;

import int221.sit.taskboard.entities.Users;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserLogin {
    @Size(max = 50)
    private String username;
    @Size(max = 14)
    private String password;
}
