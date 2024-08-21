package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.UserLogin;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.Jwt.JwtUserDetailsService;
import int221.sit.taskboard.entities.Users;
import int221.sit.taskboard.exceptions.NotCreatedException;
import int221.sit.taskboard.repositories.auth.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("")
    public ResponseEntity<String> createAuthenticationToken(@Valid @RequestBody UserLogin userLogin) {
        Users user = userRepository.findByUsername(userLogin.getUsername());

        if (user == null) {
            throw new NotCreatedException("Username or Password is invalid!");
        }

        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword())
            );

            // Load the user details
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userLogin.getUsername());

            // Generate the JWT token
            String token = jwtTokenUtil.generateToken(user);

            // Return the token
            return ResponseEntity.ok(token);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(401).body("Username or Password is invalid!");
        }
    }
}
