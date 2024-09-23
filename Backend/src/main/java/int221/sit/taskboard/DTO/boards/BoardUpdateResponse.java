package int221.sit.taskboard.DTO.boards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardUpdateResponse {
    private String boardId;
    private String message;
    private String visibility;
}
