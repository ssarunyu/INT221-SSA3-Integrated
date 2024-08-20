package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.UserDto;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.Jwt.JwtUserDetailsService;
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
//    For get users by using spring boot
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("")
    public ResponseEntity<Object> login(@RequestBody @Valid UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
        );
        if(!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Invalid user or password ! ! !");
        }
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userDto.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }

}
