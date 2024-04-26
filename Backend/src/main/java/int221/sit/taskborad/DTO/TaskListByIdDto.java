package int221.sit.taskborad.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import int221.sit.taskborad.entities.TaskList;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@JsonPropertyOrder({"title", "description", "assignee", "status", "createdOn", "updatedOn"})
public class TaskListByIdDto {
    private String Title;
    private String Description;
    private String Assignee;
    private TaskList.TaskStatus status;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}
