package int221.sit.taskboard.DTO.boards;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import int221.sit.taskboard.DTO.UserListResponse;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@JsonPropertyOrder({"id", "boardName", "visibility", "owner"})
public class BoardDTO {
    @Size(max = 10)
    private String Id;
    @Size(max = 120)
    private String boardName;
    private String visibility;
    private UserListResponse owner;
}
