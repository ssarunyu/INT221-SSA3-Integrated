package int221.sit.taskboard.DTO.collaborators;

import int221.sit.taskboard.entities.itbkk_db.Collaborator;
import int221.sit.taskboard.entities.itbkk_db.SharedBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Data
@AllArgsConstructor
public class CollaboratorResponseDTO {
    private String oid;
    private String name;
    private String email;
    private Collaborator.AccessRight accessRight;
    private ZonedDateTime addedOn;
}
