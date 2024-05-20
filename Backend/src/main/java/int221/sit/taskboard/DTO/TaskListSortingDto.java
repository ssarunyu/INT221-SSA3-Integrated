package int221.sit.taskboard.DTO;

import int221.sit.taskboard.entities.StatusList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class TaskListSortingDto {
    private Integer id;
    private String title;
    private String assignees;
    private StatusList status;
}
