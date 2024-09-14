package int221.sit.taskboard.repositories.task;

import int221.sit.taskboard.entities.itbkk_db.UserList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserListRepository extends JpaRepository<UserList, String> {
    UserList findByUsername(String username);

    UserList findByUserListId(String userListId);
}
