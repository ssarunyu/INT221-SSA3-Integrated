package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.UserLogin;
import int221.sit.taskboard.Jwt.AuthResponse;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.Jwt.JwtUserDetailsService;
import int221.sit.taskboard.entities.Users;
import int221.sit.taskboard.exceptions.NotCreatedException;
import int221.sit.taskboard.repositories.auth.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:5173")
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
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid UserLogin userlogin) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userlogin.getUsername(), userlogin.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Users user = userRepository.findByUsername(userlogin.getUsername());

            String token = jwtTokenUtil.generateToken(user);

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception ex) {
            throw new NotCreatedException("Username or Password is invalid !");
        }
    }
}