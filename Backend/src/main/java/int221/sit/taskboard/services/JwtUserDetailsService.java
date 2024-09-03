package int221.sit.taskboard.services;

import int221.sit.taskboard.Jwt.AuthUser;
import int221.sit.taskboard.entities.Users;
import int221.sit.taskboard.exceptions.NotCreatedException;
import int221.sit.taskboard.repositories.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new UsernameNotFoundException("The Username or Password is incorrect");
        }

        return new AuthUser(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
