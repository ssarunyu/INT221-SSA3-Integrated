package int221.sit.taskboard.DTO;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BoardDTO {
    private String boardId;
    @Size(max = 120)
    private String boardName;
    private UserListResponse owner;
}
