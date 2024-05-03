package int221.sit.taskboard.DTO;

import int221.sit.taskboard.entities.TaskList;
import lombok.Data;

@Data
public class NewTaskListDto {
    private Integer id;
    private String Title;
    private String Description;
    private String Assignees;
    private TaskList.TaskStatus status = TaskList.TaskStatus.NO_STATUS;
}
