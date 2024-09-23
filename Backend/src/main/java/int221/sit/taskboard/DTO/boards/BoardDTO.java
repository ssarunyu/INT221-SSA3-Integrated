package int221.sit.taskboard.DTO.boards;

import int221.sit.taskboard.DTO.UserListResponse;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BoardDTO {
    @Size(max = 10)
    private String boardId;
    @Size(max = 120)
    private String boardName;
    private String visibility;
    private UserListResponse owner;
}
