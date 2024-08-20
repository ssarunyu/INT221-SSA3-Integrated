package int221.sit.taskboard.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import int221.sit.taskboard.project_management.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"id", "title", "assignees", "status"})
@Data
public class TaskShortDetail {
    private Integer id;
    private String title;
    private String assignees;
    private StatusList status;
}
