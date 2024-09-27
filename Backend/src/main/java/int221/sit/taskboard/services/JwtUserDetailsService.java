package int221.sit.taskboard.services;

import int221.sit.taskboard.Jwt.AuthUser;
import int221.sit.taskboard.entities.itbkk_db.UserList;
import int221.sit.taskboard.entities.itbkk_shared.Users;
import int221.sit.taskboard.exceptions.CustomUsernameNotFoundException;
import int221.sit.taskboard.repositories.auth.UserRepository;
import int221.sit.taskboard.repositories.task.UserListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserListRepository userListRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws CustomUsernameNotFoundException {
        Users user = userRepository.findByUsername(userName);

        if (user == null) {
            throw new CustomUsernameNotFoundException("The Username or Password is incorrect !!!");
        }

        UserList userList = new UserList();
        userList.setUserListId(user.getUserId());
        userList.setUsername(user.getUsername());
        userList.setName(user.getName());
        userList.setEmail(user.getEmail());
        userList.setCreatedOn(ZonedDateTime.now());
        userList.setUpdatedOn(ZonedDateTime.now());
        userListRepository.save(userList);

        return new AuthUser(user.getUsername(), user.getPassword());
    }


}
