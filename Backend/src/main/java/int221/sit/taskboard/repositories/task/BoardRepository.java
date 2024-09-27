package int221.sit.taskboard.repositories.task;

import int221.sit.taskboard.entities.itbkk_db.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Boards, String> {
    @Query("SELECT sb.board FROM SharedBoard sb WHERE sb.owner.userListId = :userId")
    List<Boards> findAllByOwnerId(@Param("userId") String userId);

    Boards findByBoardId(String boardId);


    @Query("SELECT b FROM Boards b WHERE b.ownerId = :userId")
    List<Boards> findAllBoardByUserId(@Param("userId") String userId);
}
