package int221.sit.taskboard.Jwt;

import int221.sit.taskboard.entities.Users;
import int221.sit.taskboard.exceptions.NotCreatedException;
import int221.sit.taskboard.repositories.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
            Users user = userRepository.findByUsername(userName);
            if (user == null) {
                throw new NotCreatedException("The Username or Password is incorrect");
            }

//        List<GrantedAuthority> roles = new ArrayList<>();
//        GrantedAuthority grantedAuthority = new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return user.getRole();
//            }
//        };
//        roles.add(grantedAuthority);

            UserDetails userDetails = new AuthUser(userName, user.getPassword());
            return userDetails;
        }
}
