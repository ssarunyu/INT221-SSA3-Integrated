package int221.sit.taskboard.services;

import int221.sit.taskboard.DTO.JwtRequestUser;
import int221.sit.taskboard.DTO.UserListResponse;
import int221.sit.taskboard.entities.itbkk_db.UserList;
import int221.sit.taskboard.entities.itbkk_shared.Users;
import int221.sit.taskboard.exceptions.BadRequestException;
import int221.sit.taskboard.exceptions.NotCreatedException;
import int221.sit.taskboard.repositories.auth.UserRepository;
import int221.sit.taskboard.repositories.task.UserListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserListRepository userListRepository;

    @Transactional("userAuthTransactionManager")
    public JwtRequestUser userLogin(String username, String rawPassword) {

        if (username.isEmpty() || rawPassword.isEmpty() || username == null || rawPassword == null) {
            throw new BadRequestException("Username or Password is incorrect!");
        }

        Users users = userRepository.findByUsername(username);
        Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 6000, 10);

        if (users == null || passwordEncoder.matches(rawPassword, users.getPassword())) {
            throw new NotCreatedException("Username or Password is incorrect!");
        }

        JwtRequestUser userDto = new JwtRequestUser();
        userDto.setUserName("string");
        userDto.setPassword("string");
        return userDto;
    }

    public UserListResponse getUserById(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserListResponse response = new UserListResponse();
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
//        response.setRole(user.getRole());

        return response;
    }

    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
