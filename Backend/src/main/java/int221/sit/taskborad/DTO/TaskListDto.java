package int221.sit.taskborad.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import int221.sit.taskborad.entities.TaskList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"id", "title", "assignees", "status"})
public class TaskListDto {
    private Integer id;
    private String Title;
    private String Assignees;
    private TaskList.TaskStatus status;
}
