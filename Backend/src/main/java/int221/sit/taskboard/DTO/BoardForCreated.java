package int221.sit.taskboard.DTO;
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
    private UserListResponse owner;
}
