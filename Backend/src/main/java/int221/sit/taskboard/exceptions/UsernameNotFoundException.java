package int221.sit.taskboard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameNotFoundException extends ResponseStatusException {
    public UsernameNotFoundException(String message){
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
