package int221.sit.taskboard.DTO.collaborators;

import int221.sit.taskboard.entities.itbkk_db.Collaborator;
import int221.sit.taskboard.entities.itbkk_db.SharedBoard;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AddCollaboratorRequestDTO  {
    @NotEmpty
    @Size(max = 50)
    private String email;
    private Collaborator.AccessRight accessRight;
}
