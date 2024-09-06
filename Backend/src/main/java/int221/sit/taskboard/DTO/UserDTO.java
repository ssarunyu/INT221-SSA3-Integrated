package int221.sit.taskboard.DTO;

import int221.sit.taskboard.entities.Users;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserDTO {
    private String username;
    private String name;
    private String email;
    private Users.Role role;
}
