package int221.sit.taskboard.DTO;

import int221.sit.taskboard.entities.itbkk_shared.Users;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserListResponse {
    private String userId;
    private String username;
    private String name;
    private String email;
//    private Users.Role role;
}
