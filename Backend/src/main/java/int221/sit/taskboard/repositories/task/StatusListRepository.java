package int221.sit.taskboard.repositories.task;

import int221.sit.taskboard.entities.itbkk_db.StatusList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StatusListRepository extends JpaRepository<StatusList, Integer> {
    @Query("SELECT s FROM StatusList s WHERE s.board.boardId = :boardId")
    List<StatusList> findAllStatus(String boardId);

    @Query("SELECT s FROM StatusList s WHERE s.board.boardId = :boardId AND LOWER(s.name) = LOWER(:statusName)")
    List<StatusList> findByNameAndBoard(String boardId, String statusName);

    @Query("SELECT s FROM StatusList s WHERE s.id = :statusId AND s.board.boardId = :boardId")
    Optional<StatusList> findByIdAndBoard(Integer statusId, String boardId);

    @Query("SELECT s FROM StatusList s WHERE s.id = :statusId AND s.board.boardId = :boardId")
    List<StatusList> findStatusByIdAndBoard(Integer statusId, String boardId);

    @Modifying
    @Transactional
    @Query("DELETE FROM StatusList s WHERE s.id = :statusId AND s.board.boardId = :boardId")
   void deleteByIdAndBoard(Integer statusId, String boardId);
}
