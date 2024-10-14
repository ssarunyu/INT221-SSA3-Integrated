package int221.sit.taskboard.DTO.collaborators;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class BoardWithCollaboratorsDTO {
    private String boardId;
    private String boardName;
    private List<CollaboratorResponseDTO> collaborators;
}
