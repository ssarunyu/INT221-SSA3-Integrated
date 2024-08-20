package int221.sit.taskboard.project_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskListRepository extends JpaRepository<TaskList, Integer> {
    List<TaskList> findByStatusId(Integer statusId);

    @Query("select t from TaskList t join t.status s order by s.name asc")
    List<TaskList> findAllTasksSortedByStatusNameAsc();

    @Query("select t from TaskList t join t.status s order by s.name desc")
    List<TaskList> findAllTasksSortedByStatusNameDesc();

    @Query("select t from TaskList t order by t.createdOn asc")
    List<TaskList> findAllTasksSortedByCreatedDateAsc();

    List<TaskList> findByStatusNameIn(List<String> statusNames);

}
