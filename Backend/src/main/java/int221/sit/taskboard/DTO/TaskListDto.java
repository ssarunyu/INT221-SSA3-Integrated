package int221.sit.taskboard.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import int221.sit.taskboard.entities.TaskList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"id", "title", "assignees", "status"})
@Data
public class TaskListDto {
    private Integer id;
    private String Title;
    private String Assignees;
    private TaskList.TaskStatus status;
}
