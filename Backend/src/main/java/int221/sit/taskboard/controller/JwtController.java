package int221.sit.taskboard.controller;

import int221.sit.taskboard.Jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JwtController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/decode-token")
    public Map<String, Object> decodeToken(@RequestHeader("Authorization") String token) {
        // Remove the "Bearer " prefix from the token
        token = token.substring(7);

        return jwtTokenUtil.getAllClaimsFromToken(token);
    }
}
