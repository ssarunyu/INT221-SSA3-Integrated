package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.JwtRequestUser;
import int221.sit.taskboard.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = {"http://localhost:5173","http://ip23ssa3.sit.kmutt.ac.th", "https://intproj23.sit.kmutt.ac.th/"})
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("")
    private ResponseEntity<JwtRequestUser> UserLogin(@Valid @RequestBody JwtRequestUser userDto) {
        JwtRequestUser user = userService.userLogin(userDto.getUserName(), userDto.getPassword());
        return ResponseEntity.ok(user);
    }
}
