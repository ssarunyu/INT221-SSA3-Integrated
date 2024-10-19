package int221.sit.taskboard.repositories.task;

import int221.sit.taskboard.entities.itbkk_db.Boards;
import int221.sit.taskboard.entities.itbkk_db.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {
    List<Collaborator> findByBoard(Boards board);
    List<Collaborator> findAllByUser_UserListId(String userListId);
    Collaborator findCollaboratorByCollabId(Long collabId);
}
