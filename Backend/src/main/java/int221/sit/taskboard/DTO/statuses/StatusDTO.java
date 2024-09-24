package int221.sit.taskboard.DTO.statuses;

import int221.sit.taskboard.DTO.boards.BoardShortDetail;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class StatusDTO {
    private Integer id;
    private String name;
    private String description;
    private String color;
    private BoardShortDetail board;
}
