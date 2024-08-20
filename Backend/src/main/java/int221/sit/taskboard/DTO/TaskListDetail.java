package int221.sit.taskboard.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import int221.sit.taskboard.project_management.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@JsonPropertyOrder({"id", "title", "description", "assignees", "status", "createdOn", "updatedOn"})
public class TaskListDetail {
    private Integer id;
    private String title;
    private String description;
    private String assignees;
    private StatusList status;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}
