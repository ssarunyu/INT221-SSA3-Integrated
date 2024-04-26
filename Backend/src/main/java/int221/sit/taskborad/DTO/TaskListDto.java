package int221.sit.taskborad.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import int221.sit.taskborad.entities.TaskList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"title", "assignee", "status"})
public class TaskListDto {
    private String Title;
    private String Assignee;
    private TaskList.TaskStatus status;
}
