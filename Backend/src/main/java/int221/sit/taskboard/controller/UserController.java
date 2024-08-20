package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.UserDto;
import int221.sit.taskboard.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("")
    private ResponseEntity<UserDto> UserLogin(@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.UserLogin(userDto.getUsername(), userDto.getPassword());
        return ResponseEntity.ok(user);
    }
}
