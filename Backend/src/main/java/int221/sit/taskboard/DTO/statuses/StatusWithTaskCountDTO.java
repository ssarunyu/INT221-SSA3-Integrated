package int221.sit.taskboard.DTO.statuses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class StatusWithTaskCountDTO {
    private Integer id;
    private String name;
    private String boardId;
    private String taskCount;
}
