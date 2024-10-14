package int221.sit.taskboard.repositories.task;

import int221.sit.taskboard.entities.itbkk_db.Boards;
import int221.sit.taskboard.entities.itbkk_db.SharedBoard;
import int221.sit.taskboard.entities.itbkk_db.UserList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharedBoardRepository extends JpaRepository<SharedBoard, String> {
    SharedBoard findByBoard(Boards board);
    SharedBoard findByBoardAndOwner(Boards board, UserList owner);

}
