package int221.sit.taskboard.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import int221.sit.taskboard.entities.itbkk_db.Boards;
import int221.sit.taskboard.entities.itbkk_db.StatusList;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@JsonPropertyOrder({"id", "title", "description", "assignees", "status", "createdOn", "updatedOn"})
public class TaskListDetail {
    private Integer id;
    @Size(max = 100, message = "title size must be between 0 and 100.")
    private String title;
    @Size(max = 100, message = "description size must be between 0 and 500.")
    private String description;
    @Size(max = 30, message = "assignees size must be between 0 and 30.")
    private String assignees;
    private StatusDTO status;
    private BoardShortDetail board;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}
