package int221.sit.taskboard.DTO.boards;
import int221.sit.taskboard.DTO.UserListResponse;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BoardForCreated {
    @Size(max = 10)
    private String id;
    @Size(max = 120)
    private String name;
    private String visibility;
    private UserListResponse owner;
}
