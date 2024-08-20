package int221.sit.taskboard.repositories.auth;

import int221.sit.taskboard.DTO.UserLogin;
import int221.sit.taskboard.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {
    Users findByUsername(String username);

    Optional<UserLogin> findByEmail(String email);
}
