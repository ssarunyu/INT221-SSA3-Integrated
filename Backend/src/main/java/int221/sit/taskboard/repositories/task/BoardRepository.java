package int221.sit.taskboard.repositories.task;

import int221.sit.taskboard.entities.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Boards, String> {
}
