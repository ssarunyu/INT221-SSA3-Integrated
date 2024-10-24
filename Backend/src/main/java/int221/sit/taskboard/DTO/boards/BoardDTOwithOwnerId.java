package int221.sit.taskboard.DTO.boards;

import int221.sit.taskboard.DTO.UserListResponse;
import int221.sit.taskboard.entities.itbkk_db.UserList;
import jakarta.validation.constraints.Size;

public class BoardDTOwithOwnerId {
    @Size(max = 10)
    private String boardId;
    @Size(max = 120)
    private String boardName;
    private String visibility;
    private UserList owner;
}
