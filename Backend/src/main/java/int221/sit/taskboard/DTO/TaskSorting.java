package int221.sit.taskboard.DTO;

import int221.sit.taskboard.entities.itbkk_db.StatusList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class TaskSorting {
    private Integer id;
    private String title;
    private String assignees;
    private StatusList status;
}
