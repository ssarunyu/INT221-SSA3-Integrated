package int221.sit.taskboard.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import int221.sit.taskboard.entities.StatusList;
import int221.sit.taskboard.entities.TaskList;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@JsonPropertyOrder({"id", "title", "description", "assignees", "status", "createdOn", "updatedOn"})
public class TaskListByIdDto {
    private Integer id;
    private String Title;
    private String Description;
    private String Assignees;
    private StatusList status;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}
