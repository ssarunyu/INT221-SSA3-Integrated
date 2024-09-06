package int221.sit.taskboard.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BoardDTO {
    private String boardId;
    private String boardName;
    private UserDTO owner;
}
