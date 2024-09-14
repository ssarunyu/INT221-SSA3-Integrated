package int221.sit.taskboard.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import int221.sit.taskboard.entities.itbkk_db.StatusList;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"id", "title", "assignees", "status"})
@Data
public class TaskShortDetail {
    private Integer id;
    @Size(max = 100, message = "title size must be between 0 and 100.")
    private String title;
    @Size(max = 30, message = "assignees size must be between 0 and 30.")
    private String assignees;
    private StatusList status;
}
