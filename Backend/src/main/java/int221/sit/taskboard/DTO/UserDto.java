package int221.sit.taskboard.DTO;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserDto {
    @Size(max = 50)
    private String username;
    @Size(max = 14)
    private String password;
}
