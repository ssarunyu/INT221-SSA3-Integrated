package int221.sit.taskboard.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import int221.sit.taskboard.entities.StatusList;
import int221.sit.taskboard.entities.TaskList;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@JsonPropertyOrder({"id", "title", "description", "assignees", "status"})
public class NewTaskListDto {
    private Integer id;
    private String Title;
    private String Description;
    private String Assignees;
    private StatusList status;
}
