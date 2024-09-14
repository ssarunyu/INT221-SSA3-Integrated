package int221.sit.taskboard.repositories.auth;

import int221.sit.taskboard.entities.itbkk_shared.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, String> {
    Users findByUserId(String userId);
    Users findByUsername(String username);
}
