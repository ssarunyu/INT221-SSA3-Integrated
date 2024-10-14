package int221.sit.taskboard.DTO.collaborators;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class CollaboratorListResponseDTO {
    private List<CollaboratorResponseDTO> collaborators;
}
