package int221.sit.taskboard.repositories.task;

import int221.sit.taskboard.entities.itbkk_db.Boards;
import int221.sit.taskboard.entities.itbkk_db.StatusList;
import int221.sit.taskboard.entities.itbkk_db.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskListRepository extends JpaRepository<TaskList, Integer> {
    List<TaskList> findByStatusId(Integer statusId);

    List<TaskList> findByStatusNameIn(List<String> statusNames);

    @Query("SELECT t FROM TaskList t " +
            "WHERE t.board.boardId = :boardId " +
            "AND (:filterStatuses IS NULL OR t.status.name IN :filterStatuses) " +
            "ORDER BY CASE " +
            "WHEN :sortBy = 'statusName' THEN t.status.name " +
            "WHEN :sortBy = 'statusId' THEN t.status.id " +
            "WHEN :sortBy = 'id' THEN t.id " +
            "WHEN :sortBy = 'title' THEN t.title " +
            "WHEN :sortBy = 'assignees' THEN t.assignees " +
            "WHEN :sortBy = 'createdOn' THEN t.createdOn " +
            "ELSE t.createdOn END DESC")
    List<TaskList> findAllByBoardIdOrderAndFilterByDesc(
            @Param("boardId") String boardId,
            @Param("filterStatuses") List<String> filterStatuses,
            @Param("sortBy") String sortBy);

    @Query("SELECT t FROM TaskList t " +
            "WHERE t.board.boardId = :boardId " +
            "AND (:filterStatuses IS NULL OR t.status.name IN :filterStatuses) " +
            "ORDER BY CASE " +
            "WHEN :sortBy = 'statusName' THEN t.status.name " +
            "WHEN :sortBy = 'statusId' THEN t.status.id " +
            "WHEN :sortBy = 'id' THEN t.id " +
            "WHEN :sortBy = 'title' THEN t.title " +
            "WHEN :sortBy = 'assignees' THEN t.assignees " +
            "WHEN :sortBy = 'createdOn' THEN t.createdOn " +
            "ELSE t.createdOn END ASC")
    List<TaskList> findAllByBoardIdOrderAndFilterByAsc(
            @Param("boardId") String boardId,
            @Param("filterStatuses") List<String> filterStatuses,
            @Param("sortBy") String sortBy);

    @Query("SELECT t FROM TaskList t WHERE t.id = :taskID AND t.board.boardId = :boardId")
    Optional<TaskList> findByIdAndBoardId(@Param("taskID") int taskID, @Param("boardId") String boardId);

    @Query("SELECT t FROM TaskList t WHERE t.board.boardId = :boardId AND t.id = :taskId")
    Optional<TaskList> findByBoardIdAndTaskId(@Param("boardId") String boardId, @Param("taskId") Integer taskId);

}
