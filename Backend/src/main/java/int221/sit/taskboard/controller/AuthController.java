package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.JwtRequestUser;
import int221.sit.taskboard.Jwt.ResponseToken;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.entities.itbkk_shared.Users;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.exceptions.NotCreatedException;
import int221.sit.taskboard.services.JwtUserDetailsService;
import int221.sit.taskboard.repositories.auth.UserRepository;
import int221.sit.taskboard.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("")
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

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid JwtRequestUser jwtRequestUser) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequestUser.getUserName(), jwtRequestUser.getPassword())
            );

            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(jwtRequestUser.getUserName());

            Users user = userRepository.findByUsername(userDetails.getUsername());
            if (user == null) {
                // หากไม่พบผู้ใช้ให้ตอบกลับด้วย Unauthorized
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
            }

            String token = jwtTokenUtil.generateToken(user);
            String refreshToken = jwtTokenUtil.generateRefreshTokenFromToken(token);
            ResponseToken responseToken = new ResponseToken(token, refreshToken);

            return ResponseEntity.ok(responseToken);

        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException ("Username or Password is incorrect !!!");
        }
    }

    @PostMapping("/token")
    public ResponseEntity<Object> refreshToken(@RequestHeader("x-refresh-token") String refreshToken) {
        try {
            Claims claims = jwtTokenUtil.getAllClaimsFromToken(refreshToken);
            String accessToken = jwtTokenUtil.generateTokenWithClaims(claims);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("access_token", accessToken);

            return ResponseEntity.ok(responseBody);
        }catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new NotCreatedException("Refresh token failed!");
        }
    }
}