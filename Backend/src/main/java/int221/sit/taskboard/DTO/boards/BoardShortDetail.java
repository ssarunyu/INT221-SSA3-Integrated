package int221.sit.taskboard.DTO.boards;

import int221.sit.taskboard.entities.itbkk_db.UserList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BoardShortDetail {
    private String boardId;
    private String boardName;
    private UserList ownerUsername;
}
