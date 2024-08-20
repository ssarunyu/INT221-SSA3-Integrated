package int221.sit.taskboard.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import int221.sit.taskboard.entities.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@JsonPropertyOrder({"id", "title", "description", "assignees", "status"})
public class TaskAndStatusObject {
    private Integer id;
    private String title;
    private String description;
    private String assignees;
    private StatusList status;
}
