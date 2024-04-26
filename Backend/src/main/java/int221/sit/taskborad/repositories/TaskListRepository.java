package int221.sit.taskborad.repositories;

import int221.sit.taskborad.entities.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskListRepository extends JpaRepository<TaskList, Integer> {

}
