package int221.sit.taskboard.repositories.task;

import int221.sit.taskboard.entities.Boards;
import int221.sit.taskboard.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Boards, String> {
    List<Boards> findByOwner(Users owner);
    List<Boards> findByOwnerUsername(String username);
}
