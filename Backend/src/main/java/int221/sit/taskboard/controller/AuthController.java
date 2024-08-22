package int221.sit.taskboard.controller;

import int221.sit.taskboard.Jwt.JwtRequestUser;
import int221.sit.taskboard.Jwt.AuthResponse;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.Jwt.JwtUserDetailsService;
import int221.sit.taskboard.repositories.auth.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> login(@RequestBody @Valid JwtRequestUser jwtRequestUser) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequestUser.getUserName(), jwtRequestUser.getPassword())
            );

            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(jwtRequestUser.getUserName());
            String token = jwtTokenUtil.generateToken(userRepository.findByUsername(userDetails.getUsername()));
            AuthResponse authResponse = new AuthResponse(token);

            return ResponseEntity.ok(authResponse);

        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException ("user password incorrect !");
        }
    }
}