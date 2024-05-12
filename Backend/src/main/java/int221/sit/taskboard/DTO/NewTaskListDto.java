package int221.sit.taskboard.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import int221.sit.taskboard.entities.StatusList;
import int221.sit.taskboard.entities.TaskList;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "title", "description", "assignees", "status"})
public class NewTaskListDto {
    private Integer id;
    private String Title;
    private String Description;
    private String Assignees;
    private StatusList status;
}
