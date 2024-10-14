package int221.sit.taskboard.repositories.auth;

import int221.sit.taskboard.entities.itbkk_shared.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {
    Optional<Users> findByUserId(String userId);
    Users findByUsername(String username);
}
